package com.example.vocatest.controller;

import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping
    public List<UserEntity> findAllUser(){
       return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserEntity findUserById(@PathVariable("id")Long id){ 
        return userService.findUserById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserEntity> delete(@PathVariable("id")Long id){
        UserEntity userEntity = userService.findUserById(id);
        if (userEntity == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        userService.delete(userEntity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logged out successfully");
    }

}
