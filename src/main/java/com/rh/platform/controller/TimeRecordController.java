package com.rh.platform.controller;

import com.rh.platform.dto.TimeRecordDTO;
import com.rh.platform.service.TimeRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/time-records")
@RequiredArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @PostMapping
    public ResponseEntity<TimeRecordDTO> registerTime(@Valid @RequestBody TimeRecordDTO timeRecordDTO) {
        TimeRecordDTO created = timeRecordService.registerTime(timeRecordDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<TimeRecordDTO>> getRecordsByEmployeeAndMonth(
            @PathVariable Long employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(timeRecordService.getRecordsByEmployeeAndMonth(employeeId, startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeRecordDTO> getTimeRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(timeRecordService.getTimeRecordById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeRecordDTO> updateTimeRecord(@PathVariable Long id, @Valid @RequestBody TimeRecordDTO timeRecordDTO) {
        return ResponseEntity.ok(timeRecordService.updateTimeRecord(id, timeRecordDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeRecord(@PathVariable Long id) {
        timeRecordService.deleteTimeRecord(id);
        return ResponseEntity.noContent().build();
    }
}
