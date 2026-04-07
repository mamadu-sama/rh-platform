package com.rh.platform.service;

import com.rh.platform.dto.AuthenticationRequest;
import com.rh.platform.dto.AuthenticationResponse;
import com.rh.platform.dto.EmployeeDTO;
import com.rh.platform.model.Employee;
import com.rh.platform.model.Role;
import com.rh.platform.repository.EmployeeRepository;
import com.rh.platform.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(EmployeeDTO request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um funcionário com este email");
        }

        Role userRole = request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN") 
                ? Role.ADMIN 
                : Role.USER;

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(userRole);
        employee.setHourlyRate(request.getHourlyRate());

        repository.save(employee);
        
        var jwtToken = jwtService.generateToken(employee);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
                
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
