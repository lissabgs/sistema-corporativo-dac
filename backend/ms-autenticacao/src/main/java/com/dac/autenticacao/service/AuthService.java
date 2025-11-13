package com.dac.autenticacao.service;

import com.dac.autenticacao.dto.AuthRegistroDTO;
import com.dac.autenticacao.dto.LoginRequestDTO;
import com.dac.autenticacao.dto.LoginResponseDTO;
import com.dac.autenticacao.model.AuthUser;
import com.dac.autenticacao.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    // Método chamado pelo ms-usuarios
    public void registerInternal(AuthRegistroDTO dto) {
        if (authUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // 1. Gere a senha aleatória (R01)
        String senha = gerarSenhaAleatoriaNumerica(6);

        AuthUser newUser = new AuthUser();
        newUser.setEmail(dto.getEmail());
        // 2. Codifique a senha gerada
        newUser.setSenhaHash(passwordEncoder.encode(senha));
        newUser.setUsuarioId(dto.getUsuarioId());
        newUser.setTipoUsuario(dto.getPerfil());
        newUser.setStatus(true);

        authUserRepository.save(newUser);

        // 3. Envie a senha por e-mail
        emailService.enviarSenhaInicial(dto.getEmail(), senha);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        AuthUser user = authUserRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Compara a senha enviada com o hash salvo no banco
        if (!passwordEncoder.matches(dto.getSenha(), user.getSenhaHash())) {
            throw new RuntimeException("Senha inválida.");
        }

        // Gera o token
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getTipoUsuario());

        return new LoginResponseDTO(token);
    }

    // Método privado para gerar a senha
    private String gerarSenhaAleatoriaNumerica(int len) {
        Random random = new Random();
        StringBuilder senha = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            senha.append(random.nextInt(10));
        }
        return senha.toString();
    }
}