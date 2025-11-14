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

// Imports de Log (NOVOS)
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Random;

@Service
// Implementa UserDetailsService (corrige o crash do SecurityConfig)
public class AuthService implements UserDetailsService {

    // 1. Adiciona o Logger
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injetado do AppConfig

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService; // Necessário para enviar a senha

    // AuthenticationManager foi REMOVIDO para quebrar o ciclo

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        return new User(
                user.getEmail(),
                user.getSenhaHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getTipoUsuario()))
        );
    }

    /**
     * Gera a senha aqui, salva, e envia por e-mail.
     */
    public void registerInternal(AuthRegistroDTO dto) {
        if (authUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // 1. Gera a senha aleatória
        String senha = gerarSenhaAleatoriaNumerica(6);

        // 2. *** A LINHA QUE VOCÊ PEDIU ***
        // Isto vai aparecer no log do 'docker compose'
        logger.info(">>> [MS-AUTENTICACAO] Senha temporária gerada para " + dto.getEmail() + ": [" + senha + "]");

        AuthUser newUser = new AuthUser();
        newUser.setEmail(dto.getEmail());

        // 3. Codifica a senha GERADA
        newUser.setSenhaHash(passwordEncoder.encode(senha));

        newUser.setUsuarioId(dto.getUsuarioId());
        newUser.setTipoUsuario(dto.getPerfil().toString());
        newUser.setStatus(true);

        authUserRepository.save(newUser);

        // 4. Tenta enviar a senha por e-mail
        try {
            emailService.enviarSenhaInicial(dto.getEmail(), senha);
            logger.info(">>> [MS-AUTENTICACAO] Tentativa de envio de e-mail de senha.");
        } catch (Exception e) {
            // Não quebra a transação se o e-mail falhar, mas avisa no log
            logger.warn(">>> [MS-AUTENTICACAO] Falha ao enviar e-mail de senha. Verifique a configuração do MailSender. Erro: " + e.getMessage());
        }
    }

    /**
     * Verificação manual da senha (corrige o 401)
     */
    public LoginResponseDTO login(LoginRequestDTO dto) {
        AuthUser user = authUserRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Verifica a senha manualmente
        if (!passwordEncoder.matches(dto.getSenha(), user.getSenhaHash())) {
            logger.warn(">>> [MS-AUTENTICACAO] Tentativa de login falhou (senha inválida) para: " + dto.getEmail());
            throw new RuntimeException("Senha inválida.");
        }

        logger.info(">>> [MS-AUTENTICACAO] Login bem-sucedido para: " + dto.getEmail());
        logger.info(">>> [MS-AUTENTICACAO] Login bem-sucedido para: " + user.getEmail());
        logger.info(">>> [MS-AUTENTICACAO] Login bem-sucedido para: " + user.getTipoUsuario());

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getTipoUsuario());
        logger.info(">>> [MS-AUTENTICACAO] TOKEN: " + token);

        return new LoginResponseDTO(token, user.getUsuarioId(), user.getTipoUsuario());
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