package tn.st2i.user_backend.dto;

public class PasswordResetRequest {
    private String newPassword;

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
