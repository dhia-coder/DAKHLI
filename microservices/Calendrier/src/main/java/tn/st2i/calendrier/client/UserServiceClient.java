package tn.st2i.calendrier.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import tn.st2i.calendrier.dto.ClasseDto;
import tn.st2i.calendrier.dto.UserDetailsDTO;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {
    
    @GetMapping("/api/users/username/{username}")
    UserDetailsDTO loadUserByUsername(
        @PathVariable("username") String username,
        @RequestHeader("Authorization") String token
    );
    @GetMapping("/api/classes")
    List<ClasseDto> getAllClasses(@RequestHeader(value = "Authorization", required = false) String token);

    @GetMapping("/api/classes/{id}")
    ClasseDto getClasseById(@PathVariable("id") Long id,
                            @RequestHeader(value = "Authorization", required = false) String token);
}
