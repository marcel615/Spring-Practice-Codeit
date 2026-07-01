package com.sprintlog.sprintlogboot.service;

import com.sprintlog.sprintlogboot.domain.ActivityAuditLog;
import com.sprintlog.sprintlogboot.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAttempt(String action, String detail) {
        auditLogRepository.save(new ActivityAuditLog(action, detail));
    }
}
