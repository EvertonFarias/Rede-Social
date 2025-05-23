package com.example.inovaTest.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.inovaTest.models.UserModel;
import java.util.List;


public interface UserRepository extends JpaRepository<UserModel, UUID> {

    UserDetails findByLogin(String username);
    UserDetails findByEmail(String email);
    Optional<UserModel> getByLogin(String username);
    Optional<UserModel> findByLoginOrEmail(String login, String email);

    List<UserModel> findByLoginContainingIgnoreCase(String login); // pra pegar todos os usuários que tenham o login

}
