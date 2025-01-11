package com.grok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grok.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
}
