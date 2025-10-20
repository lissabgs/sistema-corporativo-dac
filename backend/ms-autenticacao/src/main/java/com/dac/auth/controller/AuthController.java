package com.dac.auth.controller;

import com.dac.auth.dto.LoginDTO;
import com.dac.auth.dto.TokenDTO;
import com.dac.auth.dto.UsuarioCadastroDTO;
import com.dac.auth.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            TokenDTO tokenDTO = usuarioService.login(loginDTO);
            return ResponseEntity.ok(tokenDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
    
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody TokenDTO tokenDTO) {
        try {
            Map<String, Object> response = usuarioService.validateToken(tokenDTO.getToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", false, "erro", e.getMessage()));  
        }
    }

    @PostMapping("/autocadastro/funcionario")
    public ResponseEntity<?> autocadastroFuncionario(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO) {
        try {
            Map<String, Object> response = usuarioService.autocadastroFuncionario(cadastroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "service", "ms-autenticacao",
            "timestamp", java.time.LocalDateTime.now().toString(),
            "environment", "development"
        ));
    }
}