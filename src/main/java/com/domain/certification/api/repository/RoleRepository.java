package com.domain.certification.api.repository;

import com.domain.certification.api.data.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role,Integer> {

    Role findOneByRole(String role);
}
