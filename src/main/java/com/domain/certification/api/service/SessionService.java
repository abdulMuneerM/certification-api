package com.domain.certification.api.service;

import com.domain.certification.api.util.dto.user.UserLoginDTO;

public interface SessionService {

    UserLoginDTO login(String email, String password);

    void logout();
}
