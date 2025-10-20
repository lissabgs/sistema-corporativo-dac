package com.dac.auth.service;

import com.dac.auth.dto.*;
import com.dac.auth.entity.Usuario;
import com.dac.auth.enums.TipoUsuario;
import com.dac.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    
    @Value("${app.security.password.default}")
    private String defaultPassword;

    @Value("${app.business.default-level}")
    private String defaultLevel;

    @Value("${app.business.default-xp}")
    private Integer defaultXp;

    @Value("${app.business.company-domain}")
    private String companyDomain;
    
    public TokenDTO login(LoginDTO loginDTO) {
        try {
            validateCompanyEmail(loginDTO.getEmail());
            
            // ✅ AUTENTICAÇÃO COM AuthenticationManager (igual professor)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
            );
            
            Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            if (!usuario.getAtivo()) {
                throw new RuntimeException("Usuário inativo");
            }
            
            String token = jwtTokenService.generateToken(usuario.getEmail(), usuario.getTipoUsuario().name());
            
            return TokenDTO.builder()
                    .token(token)
                    .status(true)
                    .tipoToken("Bearer")
                    .expiraEm(jwtTokenService.getExpirationFromToken(token).toString())
                    .build();
                    
        } catch (Exception e) {
            throw new RuntimeException("Credenciais inválidas: " + e.getMessage());
        }
    }
    
    
    public Map<String, Object> cadastrarFuncionario(UsuarioCadastroDTO cadastroDTO) {
        return cadastrarFuncionario(cadastroDTO, gerarSenhaAleatoria());
    }
    
    public Map<String, Object> cadastrarFuncionario(UsuarioCadastroDTO cadastroDTO, String senhaGerada) {
        validateCompanyEmail(cadastroDTO.getEmail());
        
        if (usuarioRepository.existsByCpf(cadastroDTO.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        
        if (usuarioRepository.existsByEmail(cadastroDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        String senhaHash = passwordEncoder.encode(senhaGerada);
        
        Usuario usuario = Usuario.builder()
                .cpf(cadastroDTO.getCpf())
                .nome(cadastroDTO.getNome())
                .email(cadastroDTO.getEmail())
                .departamento(cadastroDTO.getDepartamento())
                .cargo(cadastroDTO.getCargo())
                .senha(senhaHash)
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .nivel(defaultLevel)
                .xpTotal(defaultXp)
                .build();
        
        usuario = usuarioRepository.save(usuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Funcionário cadastrado com sucesso");
        response.put("senhaGerada", senhaGerada);
        response.put("usuario", convertToResponseDTO(usuario));
        
        return response;
    }
    
    public Map<String, Object> cadastrarInstrutor(UsuarioCadastroDTO cadastroDTO) {
        validateCompanyEmail(cadastroDTO.getEmail());
        
        if (usuarioRepository.existsByCpf(cadastroDTO.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        
        if (usuarioRepository.existsByEmail(cadastroDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        String senhaHash = passwordEncoder.encode(defaultPassword);
        
        Usuario usuario = Usuario.builder()
                .cpf(cadastroDTO.getCpf())
                .nome(cadastroDTO.getNome())
                .email(cadastroDTO.getEmail())
                .departamento(cadastroDTO.getDepartamento())
                .cargo(cadastroDTO.getCargo())
                .senha(senhaHash)
                .tipoUsuario(TipoUsuario.INSTRUTOR)
                .nivel(defaultLevel)
                .xpTotal(defaultXp)
                .build();
        
        usuario = usuarioRepository.save(usuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Instrutor cadastrado com sucesso");
        response.put("usuario", convertToResponseDTO(usuario));
        
        return response;
    }

    public Map<String, Object> autocadastroFuncionario(UsuarioCadastroDTO cadastroDTO) {
        validateCompanyEmail(cadastroDTO.getEmail());
        
        if (usuarioRepository.existsByCpf(cadastroDTO.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        if (usuarioRepository.existsByEmail(cadastroDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        String senhaGerada = gerarSenhaAleatoria();
        String senhaHash = passwordEncoder.encode(senhaGerada);

        Usuario usuario = Usuario.builder()
                .cpf(cadastroDTO.getCpf())
                .nome(cadastroDTO.getNome())
                .email(cadastroDTO.getEmail())
                .departamento(cadastroDTO.getDepartamento())
                .cargo(cadastroDTO.getCargo())
                .senha(senhaHash)
                .tipoUsuario(TipoUsuario.FUNCIONARIO) 
                .nivel(defaultLevel)
                .xpTotal(defaultXp)
                .build();

        usuario = usuarioRepository.save(usuario);

        // SIMULAR ENVIO DE EMAIL (no console)
        System.out.println("\n=== 🎉 NOVO FUNCIONÁRIO CADASTRADO ===");
        System.out.println("📧 Email: " + cadastroDTO.getEmail());
        System.out.println("🔑 Senha temporária: " + senhaGerada);
        System.out.println("💡 Instrução: Use estas credenciais para fazer login");
        System.out.println("========================================\n");

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Cadastro realizado com sucesso! Verifique seu email para as credenciais de acesso.");
        response.put("usuario", convertToResponseDTO(usuario));

        return response;
    }
    
    public Map<String, Object> cadastrarAdministrador(UsuarioCadastroDTO cadastroDTO, String senhaPersonalizada) {
        validateCompanyEmail(cadastroDTO.getEmail());
        
        if (usuarioRepository.existsByCpf(cadastroDTO.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        
        if (usuarioRepository.existsByEmail(cadastroDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        String senhaHash = passwordEncoder.encode(
            senhaPersonalizada != null ? senhaPersonalizada : defaultPassword
        );
        
        Usuario usuario = Usuario.builder()
                .cpf(cadastroDTO.getCpf())
                .nome(cadastroDTO.getNome())
                .email(cadastroDTO.getEmail())
                .departamento(cadastroDTO.getDepartamento())
                .cargo(cadastroDTO.getCargo())
                .senha(senhaHash)
                .tipoUsuario(TipoUsuario.ADMIN)
                .nivel(defaultLevel)
                .xpTotal(defaultXp)
                .build();
        
        usuario = usuarioRepository.save(usuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Administrador cadastrado com sucesso");
        response.put("usuario", convertToResponseDTO(usuario));
        
        return response;
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .filter(Usuario::getAtivo)
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public UsuarioResponseDTO buscarUsuarioPorId(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        return convertToResponseDTO(usuario);
    }
    
    public UsuarioResponseDTO atualizarUsuario(String id, UsuarioUpdateDTO updateDTO) {
        validateCompanyEmail(updateDTO.getEmail());
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(updateDTO.getEmail()) && 
            usuarioRepository.existsByEmail(updateDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso por outro usuário");
        }
        
        usuario.setNome(updateDTO.getNome());
        usuario.setEmail(updateDTO.getEmail());
        usuario.setDepartamento(updateDTO.getDepartamento());
        usuario.setCargo(updateDTO.getCargo());
        usuario.setTipoUsuario(TipoUsuario.valueOf(updateDTO.getTipoUsuario()));
        usuario.setAtivo(updateDTO.getAtivo());
        
        if (updateDTO.getXpTotal() != null) {
            usuario.setXpTotal(updateDTO.getXpTotal());
            usuario.setNivel(calcularNivel(updateDTO.getXpTotal()));
        }
        
        usuario = usuarioRepository.save(usuario);
        
        return convertToResponseDTO(usuario);
    }
    
    public void inativarUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }
    
    public Map<String, Object> validateToken(String token) {
        Boolean isValid = jwtTokenService.validateToken(token);

        if (isValid) {
            String email = jwtTokenService.getEmailFromToken(token);
            String tipoUsuario = jwtTokenService.getTipoUsuarioFromToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("status", true);  // ⬅️ MUDOU DE "valido" PARA "status"
            response.put("email", email);
            response.put("tipoUsuario", tipoUsuario);
            response.put("mensagem", "Token válido");
            return response;
        } else {
            throw new RuntimeException("Token inválido ou expirado");
        }
    }
        
    private String gerarSenhaAleatoria() {
        SecureRandom random = new SecureRandom();
        int senha = 1000 + random.nextInt(9000);
        return String.valueOf(senha);
    }
    
    private void validateCompanyEmail(String email) {
        if (!email.endsWith("@" + companyDomain)) {
            throw new RuntimeException("Email deve pertencer ao domínio da empresa: " + companyDomain);
        }
    }
    
    private String calcularNivel(Integer xpTotal) {
        if (xpTotal >= 3000) return "AVANÇADO";
        if (xpTotal >= 1000) return "INTERMEDIÁRIO";
        return "INICIANTE";
    }
    
    private UsuarioResponseDTO convertToResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .cpf(usuario.getCpf())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .departamento(usuario.getDepartamento())
                .cargo(usuario.getCargo())
                .tipoUsuario(usuario.getTipoUsuario().name())
                .ativo(usuario.getAtivo())
                .xpTotal(usuario.getXpTotal())
                .nivel(usuario.getNivel())
                .dataCadastro(usuario.getDataCadastro())
                .ultimoAcesso(usuario.getUltimoAcesso())
                .build();
    }
}