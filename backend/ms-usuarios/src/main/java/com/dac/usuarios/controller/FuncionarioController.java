package com.dac.usuarios.controller;

import com.dac.usuarios.dto.FuncionarioAutocadastroDTO;
import com.dac.usuarios.dto.UsuarioCadastroDTO;
import com.dac.usuarios.dto.UsuarioResponseDTO;
import com.dac.usuarios.dto.UsuarioUpdateDTO;
import com.dac.usuarios.exception.ResourceNotFoundException;
import com.dac.usuarios.model.Funcionario;
import com.dac.usuarios.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/funcionarios") // Rota base para os endpoints
public class FuncionarioController {

    @Autowired
    private FuncionarioService usuarioService;

    /**
     * R01: Endpoint público para autocadastro de FUNCIONARIOS.
     */
    @PostMapping("/autocadastro")
    public ResponseEntity<?> autocadastro(@Valid @RequestBody FuncionarioAutocadastroDTO dto) {
        try {
            Funcionario novoFuncionario = usuarioService.autocadastro(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(novoFuncionario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }



    /**
     * R18: Criar (ADMINISTRADOR ou INSTRUTOR)
     */
    @PostMapping
    public ResponseEntity<?> cadastrarFuncionario(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO) {
        try {
            Map<String, Object> response = usuarioService.cadastrarFuncionario(cadastroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * R18: Listar
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarFuncionarios() {
        List<UsuarioResponseDTO> funcionarios = usuarioService.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    /**
     * R18: Buscar por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarFuncionario(@PathVariable Long id) {
        try {
            UsuarioResponseDTO funcionario = usuarioService.buscarFuncionarioPorId(id);
            return ResponseEntity.ok(funcionario);
        } catch (ResourceNotFoundException e) { // Captura a exceção específica
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * R18: Atualizar
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarFuncionario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO updateDTO) {
        try {
            UsuarioResponseDTO funcionario = usuarioService.atualizarFuncionario(id, updateDTO);
            return ResponseEntity.ok(funcionario);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * R18: Inativar (Delete Lógico)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativarFuncionario(@PathVariable Long id) {
        try {
            usuarioService.inativarFuncionario(id);
            return ResponseEntity.ok(Map.of("mensagem", "Funcionário inativado com sucesso"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
}