package tn.st2i.user_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.st2i.user_backend.dto.PasswordResetRequest;
import tn.st2i.user_backend.dto.UserCreateDTO;
import tn.st2i.user_backend.dto.UserDTO;
import tn.st2i.user_backend.dto.UserDetailsDTO;
import tn.st2i.user_backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") 
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Page<UserDTO> searchUsers(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long etablissementId,
            @RequestParam(required = false) Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUsers(nom, email, etablissementId, roleId, pageable);
    }

    // UserController.java
    @GetMapping("/by-classe/{classeId}")
    public ResponseEntity<List<UserDTO>> getUsersByClasse(@PathVariable Long classeId) {
        return ResponseEntity.ok(userService.getUsersByClasse(classeId));
    }


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        if (userCreateDTO.getPassword() == null || userCreateDTO.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.createUser(userCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserCreateDTO userCreateDTO) {
        if (userCreateDTO.getPassword() != null && userCreateDTO.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        userCreateDTO.setId(id);
        return ResponseEntity.ok(userService.updateUser(userCreateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user.delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
   
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasAuthority('user.block')")
    public void blockUser(@PathVariable Long id) {
        userService.blockUser(id);
    }

    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasAuthority('user.block')")
    public void unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
    }

    @PutMapping("/{id}/role/{roleId}")
    @PreAuthorize("hasAuthority('role.assign')")
    public void assignRole(@PathVariable Long id, @PathVariable Long roleId) {
        userService.assignRole(id, roleId);
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('user.reset_password')")
    public void resetPassword(@PathVariable Long id, @RequestBody PasswordResetRequest request) {
        userService.resetPassword(id, request.getNewPassword());
    }

    @PostMapping("/{userId}/classe/{classeId}")
    @PreAuthorize("hasAuthority('user.update')")
    public ResponseEntity<UserDTO> affecterClasse(
            @PathVariable Long userId,
            @PathVariable Long classeId) {
        return ResponseEntity.ok(userService.affecterClasse(userId, classeId));
    }

    @PutMapping("/{userId}/discipline/{disciplineId}")
    @PreAuthorize("hasAuthority('user.update')")
    public ResponseEntity<UserDTO> affecterDiscipline(
            @PathVariable Long userId,
            @PathVariable Long disciplineId) {
        return ResponseEntity.ok(userService.affecterDiscipline(userId, disciplineId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetailsDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserDetailsByUsername(username));
    }
}