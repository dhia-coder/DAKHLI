package tn.st2i.user_backend.controller;

import tn.st2i.user_backend.dto.PermissionDTO;
import tn.st2i.user_backend.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "http://localhost:4200") 
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('permission.read')")
    public List<PermissionDTO> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission.read')")
    public PermissionDTO getPermission(@PathVariable Long id) {
        return permissionService.getPermission(id);
    }
}