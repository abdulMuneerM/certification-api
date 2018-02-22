package com.domain.certification.api.repository;

import com.domain.certification.api.data.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findByCredentialEmail(String email);
    Page<User> findAll(Pageable pageable);
}
