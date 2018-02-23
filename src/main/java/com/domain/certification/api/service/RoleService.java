package com.domain.certification.api.service;

import com.domain.certification.api.data.Role;

public interface RoleService {

    Role findById(Integer id);

    Role findByRoleName(String roleName);
}
