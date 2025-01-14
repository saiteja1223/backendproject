package com.movie.cenima.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movie.cenima.auth.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {


  Optional<User> findByEmail(String username);
}
