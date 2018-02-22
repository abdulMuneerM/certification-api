package com.domain.certification.api.repository;

import com.domain.certification.api.data.UserCredential;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends PagingAndSortingRepository<UserCredential,Long> {

    UserCredential findOneByEmail(String email);

    UserCredential findOneByEmailAndPassword(String email, String password);
}
