package com.nikita.forvkinternship.controllers;

import com.nikita.forvkinternship.services.AlbumsService;
import com.nikita.forvkinternship.services.AuditsService;
import com.nikita.forvkinternship.services.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/albums")
@PreAuthorize("hasAnyRole('ROLE_ALBUM', 'ROLE_ADMIN')")
public class AlbumsController {
    private final AlbumsService albumsService;
    private final AuditsService auditsService;

    @GetMapping("/")
    public ResponseEntity<Object> getAlbums(@RequestParam(required = false) Long albumId, Principal principal) {
        auditsService.makeAudit(principal,true);
        Object object = albumsService.getAlbums(albumId);
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAlbum(@RequestBody Object album,Principal principal) {
        auditsService.makeAudit(principal,true);
        albumsService.saveAlbum(album);
        return new ResponseEntity<>("Album was added", HttpStatus.OK);
    }

    @PutMapping("/{albumId}")
    public ResponseEntity<String> updateAlbum(@PathVariable Long albumId, @RequestBody Object album,Principal principal) {
        auditsService.makeAudit(principal,true);
        albumsService.updateAlbum(albumId,album);
        return new ResponseEntity<>("Album was updated", HttpStatus.OK);
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<String> deleteAlbum(@PathVariable Long albumId,Principal principal) {
        auditsService.makeAudit(principal,true);
        albumsService.deleteAlbum(albumId);
        return new ResponseEntity<>("Album was deleted", HttpStatus.OK);
    }
}
