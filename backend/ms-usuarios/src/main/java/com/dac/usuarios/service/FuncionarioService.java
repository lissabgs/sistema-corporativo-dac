package com.dac.usuarios.service;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.model.Funcionario;
import com.dac.usuarios.model.Perfil;
import com.dac.usuarios.repository.DepartamentoRepository;
import com.dac.usuarios.repository.FuncionarioRepository;
import com.dac.usuarios.dto.*;
import com.dac.usuarios.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
// import java.util.Random; // REMOVIDO
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FuncionarioService {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private String limparCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^\\d]", "");
    }

    // MÉTODO 'gerarSenhaNumerica' REMOVIDO

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

        // Lógica de senha REMOVIDA

        // 1. Criamos o DTO para o MS-Autenticacao. Senha é NULL, pois será gerada lá.
        AuthRegistroDTO authDTO = new AuthRegistroDTO(
                novoFuncionario.getEmail(),
                novoFuncionario.getPerfil(),
                novoFuncionario.getId(),
                null // <--- CORREÇÃO: Passamos NULL como 4º argumento (senha)
        );

        try {
            logger.info(">>> [MS-USUARIOS] Chamando MS-AUTENTICACAO na porta 8081 para registrar o usuário...");

            // 2. Chamamos o endpoint (a URL está correta)
            restTemplate.postForObject("http://ms-autenticacao:8081/api/auth/internal/register", authDTO, Void.class);

            logger.info(">>> [MS-USUARIOS] SUCESSO! Usuário registrado no MS-AUTENTICACAO.");
        } catch (Exception e) {
            logger.error("!!! [MS-USUARIOS] FALHA AO CHAMAR MS-AUTENTICACAO !!!", e);
            logger.error("CAUSA RAIZ: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar usuário no serviço de autenticação. Rollback realizado.", e);
        }

        // 3. Retornamos o Map SEM a senhaTemporaria
        return Map.of("funcionario", new UsuarioResponseDTO(novoFuncionario));
    }


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

        AuthRegistroDTO authDTO = new AuthRegistroDTO(
                novoFuncionario.getEmail(),
                novoFuncionario.getPerfil(),
                novoFuncionario.getId(),
                dto.getSenha() // Este é o DTO que recebe "1234" do DataInitializer
        );

        try {
            logger.info(">>> [MS-USUARIOS] Chamando MS-AUTENTICACAO na porta 8081 para registrar o usuário...");
            restTemplate.postForObject("http://ms-autenticacao:8081/api/auth/internal/register", authDTO, Void.class);
            logger.info(">>> [MS-USUARIOS] SUCESSO! Usuário registrado no MS-AUTENTICACAO.");
        } catch (Exception e) {
            logger.error("!!! [MS-USUARIOS] FALHA AO CHAMAR MS-AUTENTICACAO !!!", e);
            logger.error("CAUSA RAIZ: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar usuário no serviço de autenticação. Rollback realizado.", e);
        }

        return Map.of(
                "funcionario", new UsuarioResponseDTO(novoFuncionario),
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