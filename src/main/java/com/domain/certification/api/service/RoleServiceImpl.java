package com.domain.certification.api.service;


import com.domain.certification.api.data.Role;
import com.domain.certification.api.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Integer id) {
        return roleRepository.findOne(id);
    }
}
