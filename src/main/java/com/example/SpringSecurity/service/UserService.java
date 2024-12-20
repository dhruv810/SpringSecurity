package com.example.SpringSecurity.service;

import com.example.SpringSecurity.DTO.UserDTO;
import com.example.SpringSecurity.model.Users;
import com.example.SpringSecurity.exceptions.CustomException;
import com.example.SpringSecurity.repository.UserRepository;
import com.example.SpringSecurity.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDTO createNewUser(UserDTO userDTO) throws CustomException {
        if (this.userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }
        // encoding password
        userDTO.setPassword(this.bCryptPasswordEncoder.encode(userDTO.getPassword()));
        Users user = this.userRepository.save(UserDTO.prepareEntity(userDTO));

        return UserDTO.prepareDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<Users> users = this.userRepository.findAll();
        return users.stream().map(UserDTO::prepareDTO).toList();
    }

    public String login(UserDTO userDTO) {
        // pass in unauthenticated object and receive authenticated object
        // Using username password authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        if (authentication.isAuthenticated()) {
            // generate token here
            return jwtService.generateToken(userDTO.getUsername());
        }
        return "invalid password";
    }
}
