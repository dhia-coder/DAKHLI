package tn.st2i.user_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class UserCreateDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String address;
    private Long roleId;
    private boolean isBlocked;
    private Long etablissementId;
    private Long classeId;
    private Long disciplineId;
    private Long regionId;

    // ISO-8601 (yyyy-MM-dd) recommandé en REST
    @Past(message = "La date de naissance doit être dans le passé")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    // Pas d'enum : valeurs autorisées côté validation
    private String gender;

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public Long getRoleId() { return roleId; }
    public boolean isBlocked() { return isBlocked; }
    public Long getEtablissementId() {
        return etablissementId;
    }
    public Long getClasseId() {
        return classeId;
    }
    public Long getDisciplineId() {
        return disciplineId;
    }
    public Long getRegionId() {
        return regionId;
    }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(String address) { this.address = address; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }
    public void setEtablissementId(Long etablissementId) {
        this.etablissementId = etablissementId;
    }
    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }
    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    // getters/setters ...
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
} 