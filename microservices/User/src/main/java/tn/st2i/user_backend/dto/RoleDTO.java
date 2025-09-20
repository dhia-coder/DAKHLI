package tn.st2i.user_backend.dto;

import java.util.List;

public class RoleDTO {
    private Long id;
    private String name;
    private List<Long> permissionIds;
    private List<PermissionDTO> permissions;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Long> getPermissionIds() { return permissionIds; }
    public void setPermissionIds(List<Long> permissionIds) { this.permissionIds = permissionIds; }
    public List<PermissionDTO> getPermissions() { return permissions; }
    public void setPermissions(List<PermissionDTO> permissions) { this.permissions = permissions; }
}