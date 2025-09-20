package tn.st2i.user_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import tn.st2i.user_backend.dto.UserCreateDTO;
import tn.st2i.user_backend.dto.UserDTO;
import tn.st2i.user_backend.dto.UserDetailsDTO;
import tn.st2i.user_backend.entity.Classe;
import tn.st2i.user_backend.entity.Discipline;
import tn.st2i.user_backend.entity.Etablissement;
import tn.st2i.user_backend.entity.Region;
import tn.st2i.user_backend.entity.Role;
import tn.st2i.user_backend.entity.User;
import tn.st2i.user_backend.repository.ClasseRepository;
import tn.st2i.user_backend.repository.DisciplineRepository;
import tn.st2i.user_backend.repository.EtablissementRepository;
import tn.st2i.user_backend.repository.RegionRepository;
import tn.st2i.user_backend.repository.RoleRepository;
import tn.st2i.user_backend.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Recherche utilisateur : " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        System.out.println("Utilisateur trouvé, isBlocked=" + user.isBlocked());
        System.out.println("Role: " + (user.getRole() != null ? user.getRole().getName() : "null"));
        System.out.println("Permissions: " + (user.getRole() != null ? user.getRole().getPermissions() : "null"));
        System.out.println("Noms des permissions: " + (user.getRole() != null ? user.getRole().getPermissions().stream().map(p -> p.getName()).collect(java.util.stream.Collectors.toList()) : "null"));
        System.out.println("Password en base : " + user.getPassword());
        System.out.println("Password envoyé : 12345");
        System.out.println("PasswordEncoder.matches : " + passwordEncoder.matches("12345", user.getPassword()));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                !user.isBlocked(),
                true,
                true,
                true,
                user.getRole().getPermissions().stream()
                    .map(permission -> new org.springframework.security.core.authority.SimpleGrantedAuthority(permission.getName()))
                    .collect(java.util.stream.Collectors.toList())
        );
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setAddress(userCreateDTO.getAddress());
        user.setBlocked(userCreateDTO.isBlocked());
        user.setBirthDate(userCreateDTO.getBirthDate());      // LocalDate dans le DTO
        user.setGender(userCreateDTO.getGender());


        // Set role
        Role role = roleRepository.findById(userCreateDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);

        // Set etablissement
        if (userCreateDTO.getEtablissementId() != null) {
            Etablissement etablissement = etablissementRepository.findById(userCreateDTO.getEtablissementId())
                    .orElseThrow(() -> new RuntimeException("Etablissement not found"));
            user.setEtablissement(etablissement);
        } else {
            user.setEtablissement(null);
        }

        // Set classe
        if (userCreateDTO.getClasseId() != null) {
            Classe c = classeRepository.findById(userCreateDTO.getClasseId())
                    .orElseThrow(() -> new RuntimeException("Classe not found"));
            user.setClasse(c);

            if (user.getEtablissement() != null && c.getEtablissement() != null &&
                    !c.getEtablissement().getId().equals(user.getEtablissement().getId())) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "L'établissement de la classe ne correspond pas à l'établissement de l'utilisateur."
                );
            }
        } else {
            user.setClasse(null);
        }

        // Set discipline
        if (userCreateDTO.getDisciplineId() != null) {
            Discipline discipline = disciplineRepository.findById(userCreateDTO.getDisciplineId())
                    .orElseThrow(() -> new RuntimeException("Discipline not found"));
            user.setDiscipline(discipline);
        } else {
            user.setDiscipline(null);
        }

        // Set region
        if (userCreateDTO.getRegionId() != null) {
            Region region = regionRepository.findById(userCreateDTO.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Region not found"));
            user.setRegion(region);
        } else {
            user.setRegion(null);
        }

        return convertToDTO(userRepository.save(user));
    }

    @Transactional
public UserDTO updateUser(UserCreateDTO dto) {
    User user = userRepository.findById(dto.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
    if (dto.getLastName()  != null) user.setLastName(dto.getLastName());
    if (dto.getEmail()     != null) user.setEmail(dto.getEmail());
    if (dto.getPassword()  != null && !dto.getPassword().isBlank())
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    if (dto.getAddress()   != null) user.setAddress(dto.getAddress());
  
    if (dto.getRoleId() != null) {
        Role role = roleRepository.findById(dto.getRoleId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid roleId"));
        user.setRole(role);
    }

    if (dto.getEtablissementId() != null) {
        Etablissement etab = etablissementRepository.findById(dto.getEtablissementId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid etablissementId"));
        user.setEtablissement(etab);
    }

    if (dto.getClasseId() != null) {
        Classe classe = classeRepository.findById(dto.getClasseId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid classeId"));
        user.setClasse(classe);
        // consistency check only when both present
        if (user.getEtablissement() != null && classe.getEtablissement() != null &&
            !classe.getEtablissement().getId().equals(user.getEtablissement().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La classe n'appartient pas au même établissement que l'utilisateur.");
        }
    }

    if (dto.getDisciplineId() != null) {
        Discipline d = disciplineRepository.findById(dto.getDisciplineId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid disciplineId"));
        user.setDiscipline(d);
    }

    if (dto.getRegionId() != null) {
        Region r = regionRepository.findById(dto.getRegionId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid regionId"));
        user.setRegion(r);
    }

    return convertToDTO(userRepository.save(user));
}


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setBlocked(true);
        userRepository.save(user);
    }

    public void unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setBlocked(false);
        userRepository.save(user);
    }

    public void assignRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public Page<UserDTO> searchUsers(String nom, String email, Long etablissementId, Long roleId, Pageable pageable) {
        Specification<User> spec = Specification.where(null);
        if (nom != null && !nom.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("firstName")), "%" + nom.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("lastName")), "%" + nom.toLowerCase() + "%")
            ));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }
        if (etablissementId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("etablissement").get("id"), etablissementId));
        }
        if (roleId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("role").get("id"), roleId));
        }
        return userRepository.findAll(spec, pageable).map(this::convertToDTO);
    }

    public UserDTO affecterClasse(Long userId, Long classeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Classe classe = classeRepository.findById(classeId)
            .orElseThrow(() -> new RuntimeException("Classe not found"));
        user.setClasse(classe);
        userRepository.save(user);
        return convertToDTO(user);
    }

    public UserDTO affecterDiscipline(Long userId, Long disciplineId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Discipline discipline = disciplineRepository.findById(disciplineId)
            .orElseThrow(() -> new RuntimeException("Discipline not found"));
        user.setDiscipline(discipline);
        userRepository.save(user);
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO(user);
        if (user.getEtablissement() != null) {
            dto.setEtablissementId(user.getEtablissement().getId());
            dto.setEtablissementNom(user.getEtablissement().getNom());
        }
        if (user.getClasse() != null) {
            dto.setClasseId(user.getClasse().getId());
            dto.setClasseNom(user.getClasse().getNom());
        }
        if (user.getDiscipline() != null) {
            dto.setDisciplineId(user.getDiscipline().getId());
            dto.setDisciplineNom(user.getDiscipline().getNom());
        }
        if (user.getRegion() != null) {
            dto.setRegionId(user.getRegion().getId());
            dto.setRegionNom(user.getRegion().getNom());
        }
        return dto;
    }

    public UserDetailsDTO getUserDetailsByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<String> authorities = user.getRole() != null ? 
            user.getRole().getPermissions().stream()
                .map(permission -> permission.getName())
                .collect(Collectors.toList()) : 
            List.of();
        
        return new UserDetailsDTO(
            user.getEmail(),
            user.getEmail(),
            authorities,
            !user.isBlocked()
        );
    }

    // UserService.java
    public List<UserDTO> getUsersByClasse(Long classeId) {
        return userRepository.findByClasse_Id(classeId)
                .stream()
                .map(UserDTO::new)
                .toList();
    }

}
