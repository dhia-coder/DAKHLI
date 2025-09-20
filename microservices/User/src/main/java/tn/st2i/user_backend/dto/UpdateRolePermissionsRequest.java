package tn.st2i.user_backend.dto;

import java.util.Set;

public class UpdateRolePermissionsRequest {
    private Set<String> permissions;

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
} 