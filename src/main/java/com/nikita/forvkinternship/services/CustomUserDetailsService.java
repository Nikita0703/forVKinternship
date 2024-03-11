package com.nikita.forvkinternship.services;

import com.nikita.forvkinternship.models.User;
import com.nikita.forvkinternship.models.enums.ERole;
import com.nikita.forvkinternship.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
             //   .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
        return build(user);
    }

    public User loadUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public static User build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (ERole role : user.getRoles()) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.name());
            authorities.add(simpleGrantedAuthority);
        }

        return new User(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
