package com.domain.certification.api.service;

import com.domain.certification.api.data.Role;
import com.domain.certification.api.data.User;
import com.domain.certification.api.data.UserCredential;
import com.domain.certification.api.exception.DuplicateEntityException;
import com.domain.certification.api.exception.EntityNotFoundException;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.repository.UserCredentialRepository;
import com.domain.certification.api.repository.UserRepository;
import com.domain.certification.api.util.CoreService;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import com.domain.certification.api.util.spec.UserSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CoreService coreService;

    public UserServiceImpl(UserRepository userRepository, UserCredentialRepository userCredentialRepository,
                           RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, CoreService coreService) {
        this.userRepository = userRepository;
        this.userCredentialRepository = userCredentialRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.coreService = coreService;
    }

    @Transactional(readOnly = true)
    @Override
    public User findOne(String id) {
        if (StringUtils.isEmpty(id)) {
            LOG.error("id is empty. Please enter valid id.");
            throw new UnProcessableEntityException("id is empty. Please enter valid id.");
        }

        User user = userRepository.findOne(id);
        if (user == null) {
            LOG.error("User does not exist with id {}", id);
            throw new EntityNotFoundException("User does not exist with id " + id);
        }

        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            LOG.error("id is empty. Please enter valid id.");
            throw new UnProcessableEntityException("email is empty. Please enter valid email.");
        }

        User user = userRepository.findByCredentialEmail(email);
        if (user == null) {
            LOG.error("User does not exist with email {}", email);
            throw new EntityNotFoundException("User does not exist with email " + email);
        }

        return user;
    }

    @Transactional
    @Override
    public User save(UserRequestDTO userRequestDTO) {
        UserCredential existingUsername = userCredentialRepository.findOneByEmail(userRequestDTO.getEmail());

        if (existingUsername != null) {
            LOG.error("Email already exists {}", userRequestDTO.getEmail());
            throw new DuplicateEntityException("Email already exists.");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(userRequestDTO.getName());

        Role role = roleService.findByRoleName(userRequestDTO.getRoleType().name());
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        UserCredential userCredential = new UserCredential();
        userCredential.setEmail(userRequestDTO.getEmail());
        userCredential.setPassword(getEncryptedPassword(userRequestDTO.getPassword()));
        userCredential.setUser(user);
        user.setCredential(userCredential);

        user.setCreatedAt(coreService.getCurrentEpochSeconds());
        return userRepository.save(user);
    }

    @Override
    public Page<User> findAll(String searchFilter, Pageable pageable) {
        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        Matcher matcher = coreService.searchPatternMatcher(searchFilter);
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }

        Specification<User> spec = builder.build();

        Page<User> users;
        try {
            users = userRepository.findAll(spec, pageable);
        } catch (InvalidDataAccessApiUsageException e) {
            LOG.error("Exception: {}", e);
            throw new UnProcessableEntityException("Could not validate request");
        }

        return users;
    }

    private String getEncryptedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
