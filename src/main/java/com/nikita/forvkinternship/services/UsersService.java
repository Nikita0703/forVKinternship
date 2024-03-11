package com.nikita.forvkinternship.services;

import com.nikita.forvkinternship.models.User;
import com.nikita.forvkinternship.models.enums.ERole;
import com.nikita.forvkinternship.payload.request.SingUpRequest;
import com.nikita.forvkinternship.repositories.AuditRepository;
import com.nikita.forvkinternship.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    @Value("${my.url}")
    public String BASE_URL;

    private final RestTemplate restTemplate;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuditRepository auditRepository;

    public Object getUsers(int userId){
        String url = BASE_URL + "/api/users";

        if (userId != 0) {
            url += "?userId=" + userId;
            return restTemplate.getForObject(url, Object.class);
        }

        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Object>>() {
                });
    }

    public void saveUser(Object user){
        String url = BASE_URL + "/api/users";
        restTemplate.postForEntity(url, user, String.class);
    }

    public void updateUser(Long userId,Object user){
        String url = BASE_URL + "/api/users"+"/"+userId;
        restTemplate.put(url, user);
    }

    public void deleteUser(Long userId){
        String url = BASE_URL + "/api/users"+"/"+userId;
        restTemplate.delete(url);
    }

    public void createUser(SingUpRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

      //  try {
        userRepository.save(user);
       // } catch (Exception exception) {
            //log.error("Error during registration {}", exception.getMessage());
            // throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
       // }

    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username);
    }

    public User getUserByUsername(String username){
       User user=  userRepository.findUserByUsername(username);
        return user;
    }
}
