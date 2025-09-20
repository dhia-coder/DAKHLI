package tn.st2i.user_backend.repository;

import tn.st2i.user_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u JOIN FETCH u.role r JOIN FETCH r.permissions WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    List<User> findByClasse_Id(Long classeId);
    List<User> findByRole_NameIgnoreCase(String roleName);
    List<User> findByDisciplineId(Long disciplineId);
    List<User> findByDiscipline_IdAndRegion_Id(Long disciplineId, Long regionId);

    List<User> findAllByRegion_Id(Long regionId);
}