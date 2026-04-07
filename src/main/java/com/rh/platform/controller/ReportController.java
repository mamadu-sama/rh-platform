package com.rh.platform.controller;

import com.rh.platform.dto.MonthlyReportDTO;
import com.rh.platform.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly/{employeeId}")
    public ResponseEntity<MonthlyReportDTO> getMonthlyReport(
            @PathVariable Long employeeId,
            @RequestParam int year,
            @RequestParam int month) {
        
        MonthlyReportDTO report = reportService.generateMonthlyReport(employeeId, year, month);
        return ResponseEntity.ok(report);
    }
}
