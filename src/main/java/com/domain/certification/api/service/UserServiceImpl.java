package com.domain.certification.api.service;

import com.domain.certification.api.data.Role;
import com.domain.certification.api.data.User;
import com.domain.certification.api.data.UserCredential;
import com.domain.certification.api.repository.UserCredentialRepository;
import com.domain.certification.api.repository.UserRepository;
import com.domain.certification.api.util.dto.UserRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByCredentialEmail(email);
    }

    @Transactional
    @Override
    public User save(UserRequestDTO userRequestDTO) {

        UserCredential existingUsername = userCredentialRepository.findOneByEmail(userRequestDTO.getEmail());

        if (existingUsername != null) {
            throw new RuntimeException("Username already exists.");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(userRequestDTO.getName());

        Role role = roleService.findById(2);
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
