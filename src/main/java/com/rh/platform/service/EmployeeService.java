package com.rh.platform.service;

import com.rh.platform.dto.EmployeeDTO;
import com.rh.platform.model.Employee;
import com.rh.platform.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.findByEmail(employeeDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um funcionário com este email");
        }
        
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setHourlyRate(employeeDTO.getHourlyRate());
        
        Employee saved = employeeRepository.save(employee);
        return convertToDTO(saved);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));
        return convertToDTO(employee);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

        // Verifica se o novo email já pertence a outro funcionário
        employeeRepository.findByEmail(employeeDTO.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(employee.getId())) {
                        throw new IllegalArgumentException("Já existe um funcionário com este email");
                    }
                });

        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setHourlyRate(employeeDTO.getHourlyRate());

        Employee updated = employeeRepository.save(employee);
        return convertToDTO(updated);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Funcionário não encontrado");
        }
        employeeRepository.deleteById(id);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getHourlyRate());
    }
}
