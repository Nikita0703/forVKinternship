package com.nikita.forvkinternship;

import com.nikita.forvkinternship.controllers.AuthController;
import com.nikita.forvkinternship.payload.request.LoginRequest;
import com.nikita.forvkinternship.payload.request.SingUpRequest;
import com.nikita.forvkinternship.payload.response.JWTTokenSuccessResponse;
import com.nikita.forvkinternship.payload.response.MessageResponse;
import com.nikita.forvkinternship.security.JWTTokenProvider;
import com.nikita.forvkinternship.services.UsersService;
import com.nikita.forvkinternship.validation.ResponseErrorValidation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import org.springframework.security.core.Authentication;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthTests {

    @Mock
    private ResponseErrorValidation responseErrorValidation;

    @Mock
    private UsersService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testAuthenticateUser() {

        LoginRequest loginRequest = new LoginRequest("username", "password");


        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");


        String jwtToken = "mocked-jwt-token";


        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new JWTTokenSuccessResponse(jwtToken, true));


        BindingResult bindingResult = mock(BindingResult.class);
        lenient().when(bindingResult.hasErrors()).thenReturn(false);


        lenient().when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);


        lenient().when(jwtTokenProvider.generateWebToken(authentication)).thenReturn(jwtToken);


        ResponseEntity<Object> actualResponse = authController.authenticateUser(loginRequest, bindingResult);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

        SecurityContextTestUtil.setUpSecurityContext(jwtToken);

    }

    @Test
    public void testRegisterUser() {
        SingUpRequest singUpRequest = new SingUpRequest("username", "password");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new MessageResponse("User registered successfully"));

        BindingResult bindingResult = mock(BindingResult.class);
        lenient().when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<Object> actualResponse = authController.registerUser(singUpRequest, bindingResult);

        verify(userService, times(1)).createUser(singUpRequest);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());



    }



}
