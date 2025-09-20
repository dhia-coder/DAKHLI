package tn.st2i.user_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.user_backend.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
} 