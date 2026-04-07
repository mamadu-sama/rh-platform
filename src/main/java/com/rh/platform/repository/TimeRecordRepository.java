package com.rh.platform.repository;

import com.rh.platform.model.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
    List<TimeRecord> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
