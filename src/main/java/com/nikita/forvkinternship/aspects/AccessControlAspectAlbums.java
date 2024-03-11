package com.nikita.forvkinternship.aspects;

import com.nikita.forvkinternship.models.User;
import com.nikita.forvkinternship.models.enums.ERole;
import com.nikita.forvkinternship.services.AuditsService;
import com.nikita.forvkinternship.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessControlAspectAlbums {
    private final UsersService userService;
    private final AuditsService auditsService;

    @Pointcut("execution(* com.nikita.forvkinternship.controllers.AlbumsController.*(..)) && " +
                   "( @annotation(org.springframework.web.bind.annotation.GetMapping) || " +
                   "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
                   "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
                   "@annotation(org.springframework.web.bind.annotation.DeleteMapping) )")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object checkAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = userService.getUserByUsername(authentication.getName());
            for (ERole role:user.getRoles()) {

                if (!"ROLE_ALBUM".equals(role)) {

                    if (authentication instanceof UsernamePasswordAuthenticationToken) {
                        auditsService.makeAuditByUser(user, false);
                    }

                    return null;
                }
            }
        }
        return joinPoint.proceed();
    }
}
