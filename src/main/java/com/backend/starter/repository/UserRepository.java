package com.backend.starter.repository;

import com.backend.starter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    //@Query("SELECT e FROM User e ORDER BY e.id DESC") for desc order
    @Query("SELECT e FROM User e WHERE e.id > (SELECT min(e2.id) FROM User e2) ORDER BY e.id DESC")
    List<User> findAll();

}
