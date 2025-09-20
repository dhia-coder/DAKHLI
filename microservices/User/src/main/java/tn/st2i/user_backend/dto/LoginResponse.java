package tn.st2i.user_backend.dto;

import tn.st2i.user_backend.entity.User;

public class LoginResponse {
    private String token;
    private UserDTO user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = new UserDTO(user);
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}