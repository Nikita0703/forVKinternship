package com.nikita.forvkinternship;

import com.nikita.forvkinternship.controllers.PostsController;
import com.nikita.forvkinternship.services.AuditsService;
import com.nikita.forvkinternship.services.PostsService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;

import com.google.common.annotations.VisibleForTesting;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
//@RunWith(PowerMockRunner.class)
public class ForVKinternshipApplicationTests {
    @Mock
    private PostsService postsService;

    @Mock
    private AuditsService auditsService;

    @InjectMocks
    private PostsController postsController;

    @Test
    void testGetPosts(){
       // SecurityContextTestUtil.setUpSecurityContext("valid_token");

        when(postsService.getPosts(anyLong())).thenReturn(Collections.singletonList(any()));

        ResponseEntity<Object> response = postsController.getPosts(1L, mock(Principal.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(auditsService, times(1)).makeAudit(mock(Principal.class), eq(true));

        //SecurityContextTestUtil.clearSecurityContext();
    }

    @Test
    void testCreatePost() {
        Object post = new Object();

      //  SecurityContextTestUtil.setUpSecurityContext("valid_token");

        ResponseEntity<String> response = postsController.createPost(post, mock(Principal.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post was added", response.getBody());
        verify(auditsService, times(1)).makeAudit(mock(Principal.class), eq(true));
        verify(postsService, times(1)).savePost(post);

       // SecurityContextTestUtil.clearSecurityContext();
    }

    @Test
    void testUpdatePost() {
        Object post = new Object();

       // SecurityContextTestUtil.setUpSecurityContext("valid_token");

        ResponseEntity<String> response = postsController.updatePost(1L,  post , mock(Principal.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post was updated", response.getBody());
        verify(auditsService, times(1)).makeAudit(mock(Principal.class), eq(true));
        verify(postsService, times(1)).updatePost(1L, post);

       // SecurityContextTestUtil.clearSecurityContext();
    }

    @Test
    void testDeletePost() {
       // SecurityContextTestUtil.setUpSecurityContext("valid_token");
        ResponseEntity<String> response = postsController.deletePost(1L, mock(Principal.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post was deleted", response.getBody());
        verify(auditsService, times(1)).makeAudit(mock(Principal.class), eq(true));
        verify(postsService, times(1)).deletePost(1L);

       // SecurityContextTestUtil.clearSecurityContext();
    }


}
