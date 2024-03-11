package com.nikita.forvkinternship.services;

import com.google.common.annotations.VisibleForTesting;
import com.nikita.forvkinternship.models.Audit;
import com.nikita.forvkinternship.models.User;
import com.nikita.forvkinternship.repositories.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuditsService {
    private final UsersService usersService;
    private final AuditRepository auditRepository;

    public void makeAudit(Principal principal,boolean access){
        User user = usersService.getCurrentUser(principal);

        Audit audit= new Audit();
        audit.setUser(user);
        audit.setHasAccess(access);

        auditRepository.save(audit);

    }

    public void makeAuditByUser(User user,boolean access){

        Audit audit= new Audit();
        audit.setUser(user);
        audit.setHasAccess(access);

        auditRepository.save(audit);

    }
}
