package com.example.tx.propagation;

import com.example.tx.domain.AuditLog;
import com.example.tx.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    @Autowired private AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void logAction(String action) {
        // This method will fail if not called from an existing transaction.
        auditLogRepository.save(new AuditLog(action));
    }
}
