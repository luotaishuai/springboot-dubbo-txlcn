package com.example.people.repo;

import com.example.people.entity.PeopleAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anonymity
 * @create 2019-08-14 10:29
 **/
@Repository
public interface PeopleAccountRepo extends JpaRepository<PeopleAccount, Long> {
    PeopleAccount findByUsername(String username);
}
