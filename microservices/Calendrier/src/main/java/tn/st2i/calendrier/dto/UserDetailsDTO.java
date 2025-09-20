package tn.st2i.calendrier.dto;

import java.util.List;

public class UserDetailsDTO {
    private String username;
    private String email;
    private List<String> authorities;
    private boolean enabled;

    // Constructeurs
    public UserDetailsDTO() {}

    public UserDetailsDTO(String username, String email, List<String> authorities, boolean enabled) {
        this.username = username;
        this.email = email;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    // Getters et Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
} 