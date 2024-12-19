package com.example.SpringSecurity.controller;

import com.example.SpringSecurity.DTO.UserDTO;
import com.example.SpringSecurity.exceptions.CustomException;
import com.example.SpringSecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String greeting(HttpSession session) {
        return "Hello: " + session.getId();
    }

    @PutMapping("/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws CustomException {
        System.out.println(userDTO.toString());
        return new ResponseEntity<>(this.userService.createNewUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/csrf")
    public String getCsrf(HttpServletRequest request) {
        return "CSRF: " + request.getParameter("_CSRF");
    }
}