package com.rh.platform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecordDTO {

    private Long id;

    @NotNull(message = "O ID do funcionário é obrigatório")
    private Long employeeId;

    @NotNull(message = "A data é obrigatória")
    private LocalDate date;

    @NotNull(message = "As horas trabalhadas são obrigatórias")
    @Positive(message = "As horas trabalhadas devem ser positivas")
    private Double hoursWorked;
}
