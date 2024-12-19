package com.example.SpringSecurity.DTO;

import com.example.SpringSecurity.model.Users;

public class UserDTO {
    private Integer id;
    private String username;
    private String password;

    public UserDTO(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDTO() {
    }

    public static Users prepareEntity(UserDTO userDTO) {
        return new Users(userDTO.getUsername(), userDTO.getPassword());
    }

    public static UserDTO prepareDTO(Users user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
