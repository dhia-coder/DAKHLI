package tn.st2i.user_backend.repository;

import tn.st2i.user_backend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String name);
    Optional<Permission> findByName(String name);
}