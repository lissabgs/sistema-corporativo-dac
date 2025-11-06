package com.dac.autenticacao.controller;

import com.dac.autenticacao.dto.AuthRegistroDTO;
import com.dac.autenticacao.dto.LoginRequestDTO;
import com.dac.autenticacao.dto.LoginResponseDTO;
import com.dac.autenticacao.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint de Login público
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Endpoint de Registro (só deve ser chamado por outros microsserviços)
    @PostMapping("/internal/register")
    public ResponseEntity<Void> registerInternal(@RequestBody AuthRegistroDTO registroRequest) {
        try {
            authService.registerInternal(registroRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}