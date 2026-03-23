package com.CardMaster.modules.iam.controller;

import com.CardMaster.common.dto.ApiResponse;
import com.CardMaster.modules.iam.entity.AuditLog;
import com.CardMaster.modules.iam.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auditlogs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAllAuditLogs() {
        List<AuditLog> logs = auditLogService.getAllLogs();

        ApiResponse<List<AuditLog>> res = new ApiResponse<>();
        res.setMsg("Audit Logs Retrieved Successfully");
        res.setData(logs);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
