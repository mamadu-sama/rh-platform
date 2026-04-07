package com.rh.platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotNull(message = "A taxa horária é obrigatória")
    @Positive(message = "A taxa horária deve ser positiva")
    private BigDecimal hourlyRate;
}
