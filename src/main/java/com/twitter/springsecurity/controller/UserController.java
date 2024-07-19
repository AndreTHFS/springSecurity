package com.twitter.springsecurity.controller;

import com.twitter.springsecurity.dto.CreateUserDto;
import com.twitter.springsecurity.entities.User;
import com.twitter.springsecurity.repository.RoleRepository;
import com.twitter.springsecurity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto){
        if(dto.username().isEmpty() || dto.password().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        userService.newUser(dto);
        return ResponseEntity.ok().build();

    }


    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers(){
        if (userService.listUsers().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity <User> getById(@PathVariable(value="userId") UUID userId) {
        var getUser= userService.getById(userId);
        return ResponseEntity.ok(userService.getById(userId).get());
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<User> blockUser(@PathVariable(value = "userId") UUID id) {
           var user = userService.getById(id);
           if(user.isEmpty() || user.get().isBloqueado()){
               return ResponseEntity.notFound().build();
           }
           userService.blockUser(id);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/unlock/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<User> unlockedUser(@PathVariable(value = "userId") UUID id){
         var user = userService.getById(id);
         if(user.isEmpty() || !user.get().isBloqueado()){
             return ResponseEntity.notFound().build();
         }
         userService.unlockUser(user.get().getUserId());
         return ResponseEntity.ok().build();
    }
}
