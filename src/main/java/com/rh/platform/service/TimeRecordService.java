package com.rh.platform.service;

import com.rh.platform.dto.TimeRecordDTO;
import com.rh.platform.model.Employee;
import com.rh.platform.model.TimeRecord;
import com.rh.platform.repository.EmployeeRepository;
import com.rh.platform.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final EmployeeRepository employeeRepository;

    public TimeRecordDTO registerTime(TimeRecordDTO timeRecordDTO) {
        Employee employee = employeeRepository.findById(timeRecordDTO.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setEmployee(employee);
        timeRecord.setDate(timeRecordDTO.getDate());
        timeRecord.setHoursWorked(timeRecordDTO.getHoursWorked());

        TimeRecord saved = timeRecordRepository.save(timeRecord);
        return convertToDTO(saved);
    }

    public List<TimeRecordDTO> getRecordsByEmployeeAndMonth(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return timeRecordRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TimeRecordDTO getTimeRecordById(Long id) {
        TimeRecord timeRecord = timeRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registo de horas não encontrado"));
        return convertToDTO(timeRecord);
    }

    public TimeRecordDTO updateTimeRecord(Long id, TimeRecordDTO timeRecordDTO) {
        TimeRecord timeRecord = timeRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registo de horas não encontrado"));

        if (!timeRecord.getEmployee().getId().equals(timeRecordDTO.getEmployeeId())) {
            Employee newEmployee = employeeRepository.findById(timeRecordDTO.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Novo funcionário não encontrado"));
            timeRecord.setEmployee(newEmployee);
        }

        timeRecord.setDate(timeRecordDTO.getDate());
        timeRecord.setHoursWorked(timeRecordDTO.getHoursWorked());

        TimeRecord updated = timeRecordRepository.save(timeRecord);
        return convertToDTO(updated);
    }

    public void deleteTimeRecord(Long id) {
        if (!timeRecordRepository.existsById(id)) {
            throw new IllegalArgumentException("Registo de horas não encontrado");
        }
        timeRecordRepository.deleteById(id);
    }

    private TimeRecordDTO convertToDTO(TimeRecord timeRecord) {
        return new TimeRecordDTO(
                timeRecord.getId(),
                timeRecord.getEmployee().getId(),
                timeRecord.getDate(),
                timeRecord.getHoursWorked()
        );
    }
}
