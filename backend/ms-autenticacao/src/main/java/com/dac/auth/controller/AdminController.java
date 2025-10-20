package com.dac.auth.controller;

import com.dac.auth.dto.UsuarioCadastroDTO;
import com.dac.auth.dto.UsuarioResponseDTO;
import com.dac.auth.dto.UsuarioUpdateDTO;
import com.dac.auth.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final UsuarioService usuarioService;

    @PostMapping("/funcionarios")
    public ResponseEntity<?> criarFuncionario(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO) {
        try {
            Map<String, Object> response = usuarioService.cadastrarFuncionario(cadastroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/instrutores")
    public ResponseEntity<?> criarInstrutor(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO) {
        try {
            Map<String, Object> response = usuarioService.cadastrarInstrutor(cadastroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/administradores")
    public ResponseEntity<?> criarAdministrador(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO,
                                               @RequestParam(required = false) String senha) {
        try {
            Map<String, Object> response = usuarioService.cadastrarAdministrador(cadastroDTO, senha);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }
    
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> buscarUsuario(@PathVariable String id) {
        try {
            UsuarioResponseDTO usuario = usuarioService.buscarUsuarioPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
    
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable String id, @Valid @RequestBody UsuarioUpdateDTO updateDTO) {
        try {
            UsuarioResponseDTO usuario = usuarioService.atualizarUsuario(id, updateDTO);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
    
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> inativarUsuario(@PathVariable String id) {
        try {
            usuarioService.inativarUsuario(id);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário inativado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
}