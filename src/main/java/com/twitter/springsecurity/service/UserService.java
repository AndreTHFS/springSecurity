package com.twitter.springsecurity.service;

import com.twitter.springsecurity.dto.CreateUserDto;
import com.twitter.springsecurity.entities.Role;
import com.twitter.springsecurity.entities.User;
import com.twitter.springsecurity.repository.RoleRepository;
import com.twitter.springsecurity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void newUser(CreateUserDto dto){

        var userFromDb = userRepository.findByUsername(dto.username());
        userFromDb.ifPresentOrElse(
                user -> {
                    System.out.println("Usuário já existe");
                },
                () -> {
                    var user = new User();
                    user.setUsername(dto.username());
                    user.setPassword(passwordEncoder.encode(dto.password()));
                    user.setBloqueado(false);
                    user.setRoles(Set.of(roleRepository
                            .findByName(Role.Values.BASIC.name())));
                    userRepository.save(user);
                }
        );


    }

    @Transactional
    public void  blockUser(UUID userId) {
        var userFromDb = userRepository.findById(userId);
        if (userFromDb.get().isBloqueado()) {
           throw new RuntimeException("Usuário já está bloqueado");
        }
        userFromDb.get().setBloqueado(true);
        userRepository.save(userFromDb.get());
    }

    @Transactional
    public void unlockUser(UUID id){
        var userUnlocked = userRepository.findById(id);
        if (!userUnlocked.get().isBloqueado()){
            throw new RuntimeException("Usuário já está desbloqueado");
        }
            userUnlocked.get().setBloqueado(false);
            userRepository.save(userUnlocked.get());

    }

    @Transactional
    public List<User> listUsers(){

        return userRepository.findAll();

    }

    public Optional<User> getById(UUID userId){
         var user = userRepository.findById(userId);
         if(user.isEmpty()){
             throw new RuntimeException("Usuário não encontrado");
         }
         return user;
    }
}
