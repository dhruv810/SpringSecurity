package com.example.SpringSecurity.controller;

import com.example.SpringSecurity.DTO.UserDTO;
import com.example.SpringSecurity.exceptions.CustomException;
import com.example.SpringSecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws CustomException {
        return new ResponseEntity<>(this.userService.createNewUser(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(this.userService.login(userDTO), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }


    // CSRF is disabled so this should return null
    @GetMapping("/csrf")
    public String getCsrf(HttpServletRequest request) {
        return "CSRF: " + request.getParameter("_CSRF");
    }
}