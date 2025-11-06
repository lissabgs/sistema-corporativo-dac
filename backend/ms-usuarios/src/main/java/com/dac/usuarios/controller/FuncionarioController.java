// ... (imports) ...
import java.util.Map;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService usuarioService;

    @PostMapping("/autocadastro")
    public ResponseEntity<?> autocadastro(@Valid @RequestBody FuncionarioAutocadastroDTO dto) {
        try {
            // Modificado para receber o Map
            Map<String, Object> response = usuarioService.autocadastro(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarFuncionario(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO) {
        try {
            // Modificado para receber o Map
            Map<String, Object> response = usuarioService.cadastrarFuncionario(cadastroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    // ... (O resto dos métodos: GET, PUT, DELETE NÃO mudam) ...
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarFuncionarios() {
        List<UsuarioResponseDTO> funcionarios = usuarioService.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarFuncionario(@PathVariable Long id) {
        try {
            UsuarioResponseDTO funcionario = usuarioService.buscarFuncionarioPorId(id);
            return ResponseEntity.ok(funcionario);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

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