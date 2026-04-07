package com.rh.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportDTO {

    private Long employeeId;
    private String employeeName;
    private YearMonth month;
    private Double totalRegularHours;
    private Double totalOvertimeHours;
    private BigDecimal totalAmount;
}
