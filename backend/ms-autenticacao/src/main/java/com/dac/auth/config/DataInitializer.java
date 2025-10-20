package com.dac.auth.config;

import com.dac.auth.dto.UsuarioCadastroDTO;
import com.dac.auth.entity.Usuario;
import com.dac.auth.repository.UsuarioRepository;
import com.dac.auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.admin.default-email:admin@sistemacorporativo.com}")
    private String adminEmail;

    @Value("${app.security.admin.default-password:Admin123!}")
    private String adminPassword;

    @Value("${app.security.admin.default-name:Administrador do Sistema}")
    private String adminName;

    @Value("${app.security.admin.default-cpf:00000000000}")
    private String adminCpf;

    @Override
    public void run(String... args) throws Exception {
        criarAdministradorPadrao();
    }

    private void criarAdministradorPadrao() {
        Boolean adminExists = usuarioRepository.findAll().stream()
                .anyMatch(usuario -> usuario.getTipoUsuario().name().equals("ADMIN"));

        if (!adminExists) {
            try {
                UsuarioCadastroDTO adminDTO = UsuarioCadastroDTO.builder()
                    .cpf(adminCpf)
                    .nome(adminName)
                    .email(adminEmail)
                    .departamento("TI")
                    .cargo("Administrador do Sistema")
                    .build();

                Map<String, Object> response = usuarioService.cadastrarAdministrador(adminDTO, adminPassword);

                System.out.println("\n================================================");
                System.out.println("✅ ADMINISTRADOR PADRÃO CRIADO AUTOMATICAMENTE");
                System.out.println("📧 Email: " + adminEmail);
                System.out.println("🔑 Senha: " + adminPassword);
                System.out.println("💡 IMPORTANTE: Alterar a senha no primeiro login!");
                System.out.println("================================================\n");

            } catch (Exception e) {
                System.out.println("⚠️  Erro ao criar administrador padrão: " + e.getMessage());
            }
        } else {
            System.out.println("\n✅ Administrador já existe no sistema\n");
        }
    }
}