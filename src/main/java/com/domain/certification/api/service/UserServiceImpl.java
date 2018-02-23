package com.domain.certification.api.service;

import com.domain.certification.api.data.Role;
import com.domain.certification.api.data.User;
import com.domain.certification.api.data.UserCredential;
import com.domain.certification.api.exception.DuplicateEntityException;
import com.domain.certification.api.exception.EntityNotFoundException;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.repository.UserCredentialRepository;
import com.domain.certification.api.repository.UserRepository;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserCredentialRepository userCredentialRepository,
                           RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userCredentialRepository = userCredentialRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

        Role role = roleService.findById(2);//For student role
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        UserCredential userCredential = new UserCredential();
        userCredential.setEmail(userRequestDTO.getEmail());
        userCredential.setPassword(getEncryptedPassword(userRequestDTO.getPassword()));
        userCredential.setUser(user);
        user.setCredential(userCredential);

        user.setCreatedAt(Instant.now().getEpochSecond());
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll(String searchFilter, Pageable pageable) {
        return null;
    }

    private String getEncryptedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
