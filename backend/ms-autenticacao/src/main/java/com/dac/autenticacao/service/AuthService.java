package com.dac.autenticacao.service;

import com.dac.autenticacao.dto.AuthRegistroDTO;
import com.dac.autenticacao.dto.LoginRequestDTO;
import com.dac.autenticacao.dto.LoginResponseDTO;
import com.dac.autenticacao.model.AuthUser;
import com.dac.autenticacao.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailPublisherService emailPublisherService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return new User(
                user.getEmail(),
                user.getSenhaHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getTipoUsuario()))
        );
    }

    public void registerInternal(AuthRegistroDTO dto) {
        if (authUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        String senha;

        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            senha = dto.getSenha();
            logger.info(">>> [MS-AUTENTICACAO] Usando senha fornecida para " + dto.getEmail());
        } else {
            senha = gerarSenhaAleatoriaNumerica(6);
            logger.info(">>> [MS-AUTENTICACAO] Senha temporária gerada: " + dto.getEmail());
        }

        AuthUser newUser = new AuthUser();
        newUser.setEmail(dto.getEmail());
        newUser.setSenhaHash(passwordEncoder.encode(senha));
        newUser.setUsuarioId(dto.getUsuarioId());
        newUser.setTipoUsuario(dto.getPerfil().toString());
        newUser.setStatus(true);
        newUser.setDataCriacao(LocalDateTime.now());

        authUserRepository.save(newUser);
        logger.info(">>> [MS-AUTENTICACAO] Usuário salvo no MongoDB: " + newUser.getId());

        try {
            emailPublisherService.enviarEmailBoasVindas(
                    dto.getUsuarioId(),
                    dto.getEmail(),
                    "Usuário",
                    senha
            );
            logger.info(">>> [MS-AUTENTICACAO] Email enviado para fila RabbitMQ");
        } catch (Exception e) {
            logger.warn(">>> [MS-AUTENTICACAO] Falha ao enviar email: " + e.getMessage());
        }
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        AuthUser user = authUserRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenhaHash())) {
            logger.warn(">>> [MS-AUTENTICACAO] Login falhou para: " + dto.getEmail());
            throw new RuntimeException("Senha inválida.");
        }

        logger.info(">>> [MS-AUTENTICACAO] Login bem-sucedido: " + dto.getEmail());

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getTipoUsuario());

        return new LoginResponseDTO(token, user.getUsuarioId(), user.getTipoUsuario());
    }

    private String gerarSenhaAleatoriaNumerica(int tamanho) {
        Random random = new Random();
        StringBuilder senha = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            senha.append(random.nextInt(10));
        }
        return senha.toString();
    }
}