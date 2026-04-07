package com.rh.platform.service;

import com.rh.platform.dto.MonthlyReportDTO;
import com.rh.platform.model.Employee;
import com.rh.platform.model.TimeRecord;
import com.rh.platform.repository.EmployeeRepository;
import com.rh.platform.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final EmployeeRepository employeeRepository;
    private final TimeRecordRepository timeRecordRepository;
    
    // Considerando 8 horas como jornada de trabalho regular
    private static final double REGULAR_HOURS_PER_DAY = 8.0;
    
    // Adicional de 50% para horas extras
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");

    public MonthlyReportDTO generateMonthlyReport(Long employeeId, int year, int month) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<TimeRecord> records = timeRecordRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);

        double totalRegularHours = 0;
        double totalOvertimeHours = 0;

        for (TimeRecord record : records) {
            double hours = record.getHoursWorked();
            if (hours > REGULAR_HOURS_PER_DAY) {
                totalRegularHours += REGULAR_HOURS_PER_DAY;
                totalOvertimeHours += (hours - REGULAR_HOURS_PER_DAY);
            } else {
                totalRegularHours += hours;
            }
        }

        BigDecimal regularPay = employee.getHourlyRate().multiply(BigDecimal.valueOf(totalRegularHours));
        BigDecimal overtimeRate = employee.getHourlyRate().multiply(OVERTIME_MULTIPLIER);
        BigDecimal overtimePay = overtimeRate.multiply(BigDecimal.valueOf(totalOvertimeHours));
        
        BigDecimal totalAmount = regularPay.add(overtimePay);

        return new MonthlyReportDTO(
                employee.getId(),
                employee.getName(),
                yearMonth,
                totalRegularHours,
                totalOvertimeHours,
                totalAmount
        );
    }
}
