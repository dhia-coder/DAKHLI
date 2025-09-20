package tn.st2i.user_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.st2i.user_backend.dto.UserDTO;
import tn.st2i.user_backend.entity.Permission;
import tn.st2i.user_backend.entity.Role;
import tn.st2i.user_backend.entity.User;
import tn.st2i.user_backend.repository.PermissionRepository;
import tn.st2i.user_backend.repository.RoleRepository;
import tn.st2i.user_backend.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.HashSet;

@SpringBootApplication
public class UserBackendApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(UserBackendApplication.class);

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(UserBackendApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {
		logger.info("Début de l'initialisation des données...");
		
		try {
			// Create all permissions
			List<String> permissions = Arrays.asList(
					"user.read", "user.create", "user.update", "user.delete", "user.block",
					"role.read", "role.create", "role.update", "role.delete",
					"permission.read", "permission.update",
					"role.assign", "role.revoke",
					"user.reset_password"
			);

			logger.info("Création des permissions...");
			permissions.forEach(perm -> {
				if (!permissionRepository.existsByName(perm)) {
					Permission permission = new Permission();
					permission.setName(perm);
					Permission savedPermission = permissionRepository.save(permission);
					logger.info("Permission créée: {}", savedPermission.getName());
				} else {
					logger.info("Permission déjà existante: {}", perm);
				}
			});

			// Create superadmin role
			logger.info("Vérification du rôle SUPERADMIN...");
			if (!roleRepository.existsByName("SUPERADMIN")) {
				logger.info("Création du rôle SUPERADMIN...");
				Role superAdminRole = new Role();
				superAdminRole.setName("SUPERADMIN");
				superAdminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));
				Role savedRole = roleRepository.save(superAdminRole);
				logger.info("Rôle SUPERADMIN créé avec ID: {}", savedRole.getId());

				// Create superadmin user
				logger.info("Vérification de l'utilisateur super admin...");
				if (!userRepository.existsByEmail("admin@example.com")) {
					logger.info("Création de l'utilisateur super admin...");
					User superUser = new User();
					superUser.setFirstName("admin");
					superUser.setLastName("admin");
					superUser.setEmail("admin@example.com");
					superUser.setPassword(passwordEncoder.encode("Azerty123@"));
					superUser.setAddress("123 Admin Street");
					superUser.setRole(savedRole);
					// Ne pas définir les autres relations pour éviter les contraintes de clés étrangères
					superUser.setEtablissement(null);
					superUser.setClasse(null);
					superUser.setDiscipline(null);
					superUser.setRegion(null);
					
					User savedUser = userRepository.save(superUser);
					logger.info("Utilisateur super admin créé avec ID: {} et email: {}", 
						savedUser.getId(), savedUser.getEmail());
				} else {
					logger.info("Utilisateur super admin déjà existant");
				}
			} else {
				logger.info("Rôle SUPERADMIN déjà existant, mise à jour des permissions...");
				Role superAdminRole = roleRepository.findByName("SUPERADMIN").orElseThrow();
				superAdminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));
				roleRepository.save(superAdminRole);
				logger.info("Permissions du rôle SUPERADMIN mises à jour");
			}
			
			logger.info("Initialisation des données terminée avec succès");
			
		} catch (Exception e) {
			logger.error("Erreur lors de l'initialisation des données: ", e);
		}
	}

	// UserService.java
	public List<UserDTO> getUsersByClasse(Long classeId) {
		return userRepository.findByClasse_Id(classeId)
				.stream()
				.map(UserDTO::new)
				.toList();
	}

}