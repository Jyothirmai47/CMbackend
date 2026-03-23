package com.CardMaster.modules.iam.service;

import com.CardMaster.modules.iam.repository.AuditLogRepository;
import com.CardMaster.modules.iam.repository.UserRepository;
import com.CardMaster.modules.iam.entity.AuditLog;
import com.CardMaster.modules.iam.entity.User;
import com.CardMaster.modules.iam.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
    /**
     * Log an action by resolving the User entity from either userId or email.
     */
    public void log(String identifier, String action, String resource) {
        AuditLog log = new AuditLog();

        User user = resolveUser(identifier);
        if (user != null) {
            log.setUser(user);
            log.setMetadata("Performed by " + user.getName());
        } else {
            log.setUser(null);
            log.setMetadata("Performed by " + identifier);
        }

        log.setAction(action);
        log.setResource(resource);

        auditLogRepository.save(log);
    }

    private User resolveUser(String identifier) {
        try {
            // If identifier is numeric, treat it as userId
            Long userId = Long.parseLong(identifier);
            return userRepository.findById(userId).orElse(null);
        } catch (NumberFormatException e) {
            // Otherwise, treat identifier as email
            return userRepository.findByEmail(identifier).orElse(null);
        }
    }

}
