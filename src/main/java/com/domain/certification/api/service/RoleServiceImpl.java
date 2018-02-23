package com.domain.certification.api.service;


import com.domain.certification.api.data.Role;
import com.domain.certification.api.exception.EntityNotFoundException;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Integer id) {
        if (id == null) {
            LOG.error("id is empty. Please enter valid id.");
            throw new UnProcessableEntityException("id is empty. Please enter valid id.");
        }

        Role role = roleRepository.findOne(id);
        if (role == null) {
            LOG.error("Role does not exist with id {}", id);
            throw new EntityNotFoundException("Role does not exist with id " + id);
        }
        return role;
    }

    @Override
    public Role findByRoleName(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            LOG.error("role is empty. Please enter valid role.");
            throw new UnProcessableEntityException("role is empty. Please enter valid role.");
        }

        Role role = roleRepository.findOneByRole(roleName);
        if (role == null) {
            LOG.error("Role does not exist with role name {}", roleName);
            throw new EntityNotFoundException("Role does not exist with role name " + roleName);
        }
        return role;
    }
}
