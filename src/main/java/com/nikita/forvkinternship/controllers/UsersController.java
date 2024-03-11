package com.nikita.forvkinternship.controllers;


import com.nikita.forvkinternship.services.AuditsService;
import com.nikita.forvkinternship.services.PostsService;
import com.nikita.forvkinternship.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class UsersController {

    private final UsersService usersService;
    private final AuditsService auditsService;

    @GetMapping("/")
    public ResponseEntity<Object> getUsers(@RequestParam(required = false) Long userId, Principal principal) {
        auditsService.makeAudit(principal,true);
        Object object = usersService.getUsers(userId);
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody Object user,Principal principal) {
        auditsService.makeAudit(principal,true);
        usersService.saveUser(user);
        return new ResponseEntity<>("User was added", HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updatePost(@PathVariable Long userId, @RequestBody Object user,Principal principal) {
        auditsService.makeAudit(principal,true);
        usersService.updateUser(userId,user);
        return new ResponseEntity<>("user was updated", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId,Principal principal) {
        auditsService.makeAudit(principal,true);
        usersService.deleteUser(userId);
        return new ResponseEntity<>("User was deleted", HttpStatus.OK);
    }


}
