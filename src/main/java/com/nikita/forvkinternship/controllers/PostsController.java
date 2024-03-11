package com.nikita.forvkinternship.controllers;

import com.nikita.forvkinternship.services.AuditsService;
import com.nikita.forvkinternship.services.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@PreAuthorize("hasAnyRole('ROLE_POST', 'ROLE_ADMIN')")
public class PostsController {

    private final PostsService postsService;
    private final AuditsService auditsService;

    @GetMapping("/")
    public ResponseEntity<Object> getPosts(@RequestParam(required = false) Long postId, Principal principal) {
        auditsService.makeAudit(principal,true);
        Object object = postsService.getPosts(postId);
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Object post,Principal principal) {
        auditsService.makeAudit(principal,true);
        postsService.savePost(post);
        return new ResponseEntity<>("Post was added", HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody Object post,Principal principal) {
        auditsService.makeAudit(principal,true);
        postsService.updatePost(postId,post);
        return new ResponseEntity<>("Post was updated", HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,Principal principal) {
        auditsService.makeAudit(principal,true);
        postsService.deletePost(postId);
        return new ResponseEntity<>("Post was deleted", HttpStatus.OK);
    }
}
