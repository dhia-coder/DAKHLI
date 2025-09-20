package tn.st2i.user_backend.controller;

import tn.st2i.user_backend.dto.RoleDTO;
import tn.st2i.user_backend.service.RoleService;
import tn.st2i.user_backend.dto.UpdateRolePermissionsRequest;
import tn.st2i.user_backend.entity.Role;
import tn.st2i.user_backend.dto.PermissionDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:4200") 
public class RoleController {

    @Autowired
    private RoleService roleService;

    // ✅ Restriction : seul "admin@example.com" peut créer un rôle
    @PostMapping
    @PreAuthorize("hasAuthority('role.create')")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.createRole(roleDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role.update')")
    public RoleDTO updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return roleService.updateRole(id, roleDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role.delete')")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('role.read')")
    public RoleDTO getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @GetMapping
    // @PreAuthorize("hasAuthority('role.read')")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role.update')")
    public ResponseEntity<Role> updateRolePermissions(
            @PathVariable Long id,
            @RequestBody UpdateRolePermissionsRequest request) {
        Role updatedRole = roleService.updateRolePermissionsById(id, request.getPermissions());
        return ResponseEntity.ok(updatedRole);
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role.read')")
    public ResponseEntity<List<PermissionDTO>> getRolePermissions(@PathVariable Long id) {
        List<PermissionDTO> permissions = roleService.getRolePermissions(id);
        return ResponseEntity.ok(permissions);
    }
}
