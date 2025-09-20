package tn.st2i.user_backend.service;

import tn.st2i.user_backend.dto.RoleDTO;
import tn.st2i.user_backend.entity.Permission;
import tn.st2i.user_backend.entity.Role;
import tn.st2i.user_backend.repository.PermissionRepository;
import tn.st2i.user_backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.st2i.user_backend.exception.ResourceNotFoundException;
import tn.st2i.user_backend.dto.PermissionDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = new Role();
        mapToEntity(roleDTO, role);
        Role savedRole = roleRepository.save(role);
        return mapToDTO(savedRole);
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        mapToEntity(roleDTO, role);
        Role updatedRole = roleRepository.save(role);
        return mapToDTO(updatedRole);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public RoleDTO getRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return mapToDTO(role);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Role updateRolePermissions(String roleName, Set<String> permissionNames) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));

        Set<Permission> permissions = permissionNames.stream()
                .map(name -> permissionRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + name)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRolePermissionsById(Long id, Set<String> permissionNames) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        Set<Permission> permissions = permissionNames.stream()
                .map(name -> permissionRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + name)))
                .collect(Collectors.toSet());
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    public List<PermissionDTO> getRolePermissions(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return role.getPermissions().stream()
                .map(permission -> {
                    PermissionDTO dto = new PermissionDTO();
                    dto.setId(permission.getId());
                    dto.setName(permission.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void mapToEntity(RoleDTO roleDTO, Role role) {
        role.setName(roleDTO.getName());
        if (roleDTO.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleDTO.getPermissionIds()));
            role.setPermissions(permissions);
        }
    }

    private RoleDTO mapToDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setPermissionIds(role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toList()));
        roleDTO.setPermissions(role.getPermissions().stream()
                .map(permission -> {
                    PermissionDTO permissionDTO = new PermissionDTO();
                    permissionDTO.setId(permission.getId());
                    permissionDTO.setName(permission.getName());
                    return permissionDTO;
                })
                .collect(Collectors.toList()));
        return roleDTO;
    }
}