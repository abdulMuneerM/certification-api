package com.domain.certification.api.service;

import com.domain.certification.api.data.User;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User findOne(String id);

    User findByEmail(String email);

    User save(UserRequestDTO userRequestDTO);

    List<User> findAll(String searchFilter, Pageable pageable);
}
