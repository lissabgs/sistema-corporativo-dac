package com.dac.usuarios.service;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.model.Funcionario;
import com.dac.usuarios.model.Perfil;
import com.dac.usuarios.repository.DepartamentoRepository;
import com.dac.usuarios.repository.FuncionarioRepository;
import com.dac.usuarios.dto.*; // Importa todos os DTOs
import com.dac.usuarios.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

// Imports de Log
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FuncionarioService {

    // Adicione o Logger
    private static final Logger logger = LoggerFactory.getLogger(FuncionarioService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Remove caracteres não numéricos de uma string
    private String limparCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^\\d]", "");
    }

    // Gera a senha numérica de 6 dígitos
    private String gerarSenhaNumerica() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * R01: Autocadastro.
     */
    @Transactional
    public Map<String, Object> autocadastro(FuncionarioAutocadastroDTO dto) {
        logger.info("[MS-USUARIOS] Início do autocadastro para: " + dto.getEmail());

        Departamento depto = departamentoRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com ID: " + dto.getDepartamentoId()));
        logger.info("[MS-USUARIOS] Departamento encontrado: " + depto.getNome());

        Funcionario func = new Funcionario();
        func.setCpf(limparCPF(dto.getCpf()));
        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setCargo(dto.getCargo());
        func.setDepartamento(depto);
        func.setPerfil(Perfil.FUNCIONARIO);

        Funcionario novoFuncionario = funcionarioRepository.save(func);
        logger.info("[MS-USUARIOS] Funcionário salvo no Postgres com ID: " + novoFuncionario.getId());

        // ---- INÍCIO DA COMUNICAÇÃO ----
        String senhaNumerica = gerarSenhaNumerica();

        // --- PRINT IMPORTANTE ---
        logger.info(">>> [MS-USUARIOS] Senha gerada: [" + senhaNumerica + "]");

        AuthRegistroDTO authDTO = new AuthRegistroDTO(
                novoFuncionario.getEmail(),
                senhaNumerica,
                novoFuncionario.getPerfil(),
                novoFuncionario.getId()
        );

        try {
            logger.info(">>> [MS-USUARIOS] Chamando MS-AUTENTICACAO na porta 8081 para registrar a senha...");
            restTemplate.postForObject("http://ms-autenticacao:8081/register-internal", authDTO, Void.class);
            logger.info(">>> [MS-USUARIOS] SUCESSO! Senha registrada no MS-AUTENTICACAO.");
        } catch (Exception e) {
            logger.error("!!! [MS-USUARIOS] FALHA AO CHAMAR MS-AUTENTICACAO !!!", e);
            logger.error("CAUSA RAIZ: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar usuário no serviço de autenticação. Rollback realizado.", e);
        }
        // ---- FIM DA COMUNICAÇÃO ----

        return Map.of("funcionario", new UsuarioResponseDTO(novoFuncionario), "senhaTemporaria", senhaNumerica);
    }

    /**
     * R18: Cadastro de Usuário (Admin).
     */
    @Transactional
    public Map<String, Object> cadastrarFuncionario(UsuarioCadastroDTO dto) {
        logger.info("[MS-USUARIOS] Início do cadastro de ADMIN/INSTRUTOR para: " + dto.getEmail());

        if (dto.getPerfil() == Perfil.FUNCIONARIO) {
            throw new IllegalArgumentException("Use o endpoint /autocadastro para criar funcionários.");
        }

        Departamento depto = departamentoRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com ID: " + dto.getDepartamentoId()));

        Funcionario func = new Funcionario();
        func.setCpf(limparCPF(dto.getCpf()));
        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setCargo(dto.getCargo());
        func.setDepartamento(depto);
        func.setPerfil(dto.getPerfil());

        Funcionario novoFuncionario = funcionarioRepository.save(func);
        logger.info("[MS-USUARIOS] Usuário salvo no Postgres com ID: " + novoFuncionario.getId());

        // ---- INÍCIO DA COMUNICAÇÃO ----
        String senhaNumerica = gerarSenhaNumerica();

        // --- PRINT IMPORTANTE ---
        logger.info(">>> [MS-USUARIOS] Senha gerada: [" + senhaNumerica + "]");

        AuthRegistroDTO authDTO = new AuthRegistroDTO(
                novoFuncionario.getEmail(),
                senhaNumerica,
                novoFuncionario.getPerfil(),
                novoFuncionario.getId()
        );

        try {
            logger.info(">>> [MS-USUARIOS] Chamando MS-AUTENTICACAO na porta 8081 para registrar a senha...");
            restTemplate.postForObject("http://ms-autenticacao:8081/register-internal", authDTO, Void.class);
            logger.info(">>> [MS-USUARIOS] SUCESSO! Senha registrada no MS-AUTENTICACAO.");
        } catch (Exception e) {
            logger.error("!!! [MS-USUARIOS] FALHA AO CHAMAR MS-AUTENTICACAO !!!", e);
            logger.error("CAUSA RAIZ: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar usuário no serviço de autenticação. Rollback realizado.", e);
        }
        // ---- FIM DA COMUNICAÇÃO ----

        return Map.of(
                "funcionario", new UsuarioResponseDTO(novoFuncionario),
                "senhaTemporaria", senhaNumerica,
                "mensagem", "Usuário " + novoFuncionario.getPerfil() + " criado com sucesso."
        );
    }

    // --- O RESTO DO CÓDIGO (NÃO PRECISA MUDAR) ---

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarFuncionarios() {
        return funcionarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarFuncionarioPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));
        return new UsuarioResponseDTO(funcionario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarFuncionario(Long id, UsuarioUpdateDTO updateDTO) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        if (updateDTO.getNome() != null) func.setNome(updateDTO.getNome());
        if (updateDTO.getEmail() != null) func.setEmail(updateDTO.getEmail());
        if (updateDTO.getCargo() != null) func.setCargo(updateDTO.getCargo());
        if (updateDTO.getPerfil() != null) func.setPerfil(updateDTO.getPerfil());
        if (updateDTO.getStatus() != null) func.setStatus(updateDTO.getStatus());

        if (updateDTO.getDepartamentoId() != null) {
            Departamento depto = departamentoRepository.findById(updateDTO.getDepartamentoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com ID: " + updateDTO.getDepartamentoId()));
            func.setDepartamento(depto);
        }

        Funcionario funcionarioAtualizado = funcionarioRepository.save(func);
        return new UsuarioResponseDTO(funcionarioAtualizado);
    }

    @Transactional
    public void inativarFuncionario(Long id) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        func.setStatus(false);
        funcionarioRepository.save(func);
    }
}