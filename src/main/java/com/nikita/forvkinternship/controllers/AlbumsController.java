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
@PreAuthorize("hasAnyRole('ROLE_ALBUM', 'ROLE_ADMIN')")
public class AlbumsController {
    private final AlbumsService albumsService;
    private final AuditsService auditsService;

    @GetMapping("/api/albums/")
    public ResponseEntity<Object> getAlbums(@RequestParam(required = false) int albumId, Principal principal) {
        auditsService.makeAudit(principal,true);
        Object object = albumsService.getAlbums(albumId);
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/api/albums/")
    public ResponseEntity<String> createAlbum(@RequestBody Object album,Principal principal) {
        auditsService.makeAudit(principal,true);
        albumsService.saveAlbum(album);
        return new ResponseEntity<>("Album was added", HttpStatus.OK);
    }

    @PutMapping("/api/albums/{albumId}")
    public ResponseEntity<String> updateAlbum(@PathVariable Long albumId, @RequestBody Object album,Principal principal) {
        auditsService.makeAudit(principal,true);
        albumsService.updateAlbum(albumId,album);
        return new ResponseEntity<>("Album was updated", HttpStatus.OK);
    }

    @DeleteMapping("/api/albums/{albumId}")
    public ResponseEntity<String> deleteAlbum(@PathVariable Long albumId,Principal principal) {
        auditsService.makeAudit(principal,true);
        albumsService.deleteAlbum(albumId);
        return new ResponseEntity<>("Album was deleted", HttpStatus.OK);
    }
}
