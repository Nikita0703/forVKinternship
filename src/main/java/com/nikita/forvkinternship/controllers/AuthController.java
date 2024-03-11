package com.nikita.forvkinternship.controllers;


import com.nikita.forvkinternship.payload.response.JWTTokenSuccessResponse;
import com.nikita.forvkinternship.payload.response.MessageResponse;
import com.nikita.forvkinternship.security.JWTTokenProvider;
import com.nikita.forvkinternship.security.SecurityConstants;
import com.nikita.forvkinternship.services.UsersService;
import com.nikita.forvkinternship.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.nikita.forvkinternship.payload.request.LoginRequest;
import com.nikita.forvkinternship.payload.request.SingUpRequest;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class AuthController {

    private final ResponseErrorValidation responseErrorValidation;
    private final UsersService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;


    @PostMapping("/singin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                   BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateWebToken(authentication);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(jwt, true));
    }


    @PostMapping("/singup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SingUpRequest singUpRequest,
                                               BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (errors != null) {
            return errors;
        } else {

            userService.createUser(singUpRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        }
    }
}
