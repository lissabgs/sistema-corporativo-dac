package com.dac.autenticacao.service;

import com.dac.autenticacao.config.RabbitMQConfig;
import com.dac.autenticacao.dto.AuthRegistroDTO;
import com.dac.autenticacao.dto.EmailPayloadDTO;
import com.dac.autenticacao.dto.LoginRequestDTO;
import com.dac.autenticacao.dto.LoginResponseDTO;
import com.dac.autenticacao.model.AuthUser;
import com.dac.autenticacao.repository.AuthUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private RabbitTemplate rabbitTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca no MongoDB
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Retorna o objeto User do Spring Security
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
        // Se a senha vier preenchida (Admin/Instrutor), usa ela. Se não, gera aleatória.
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            senha = dto.getSenha();
        } else {
            senha = gerarSenhaAleatoriaNumerica(6);
        }

        AuthUser newUser = new AuthUser();
        newUser.setEmail(dto.getEmail());
        newUser.setSenhaHash(passwordEncoder.encode(senha));
        newUser.setUsuarioId(dto.getUsuarioId());
        newUser.setTipoUsuario(dto.getPerfil().toString());
        newUser.setStatus(true);

        authUserRepository.save(newUser);
        logger.info(">>> [MS-AUTENTICACAO] Usuário salvo no MongoDB: " + newUser.getEmail());

        // Enviar e-mail via RabbitMQ
        try {
            String assunto = "Bem-vindo ao Sistema DAC";
            String corpo = "Olá,\n\nSeu cadastro foi realizado com sucesso.\nSua senha de acesso é: " + senha;

            EmailPayloadDTO payload = new EmailPayloadDTO(
                    newUser.getUsuarioId(),
                    newUser.getEmail(),
                    assunto,
                    corpo
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, payload);
            logger.info(">>> [MS-AUTENTICACAO] Solicitação de e-mail enviada para a fila RabbitMQ.");

        } catch (Exception e) {
            logger.error("!!! Erro ao enviar mensagem para RabbitMQ: " + e.getMessage());
        }
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        AuthUser user = authUserRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenhaHash())) {
            throw new RuntimeException("Senha inválida.");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getTipoUsuario());
        return new LoginResponseDTO(token, user.getUsuarioId(), user.getTipoUsuario());
    }

    private String gerarSenhaAleatoriaNumerica(int len) {
        Random random = new Random();
        StringBuilder senha = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            senha.append(random.nextInt(10));
        }
        return senha.toString();
    }
}