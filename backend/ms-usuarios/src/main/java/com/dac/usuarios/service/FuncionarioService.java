package com.dac.usuarios.service;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.model.Funcionario;
import com.dac.usuarios.model.Perfil;
import com.dac.usuarios.repository.DepartamentoRepository;
import com.dac.usuarios.repository.FuncionarioRepository;
import com.dac.usuarios.dto.FuncionarioAutocadastroDTO;
import com.dac.usuarios.dto.UsuarioCadastroDTO;
import com.dac.usuarios.dto.UsuarioResponseDTO;
import com.dac.usuarios.dto.UsuarioUpdateDTO;
import com.dac.usuarios.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    // Remove caracteres não numéricos de uma string
    private String limparCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^\\d]", "");
    }

    /**
     * R01: Autocadastro.
     * Endpoint público que SEMPRE cria um FUNCIONARIO.
     */
    @Transactional
    public Funcionario autocadastro(FuncionarioAutocadastroDTO dto) {
        Departamento depto = departamentoRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com ID: " + dto.getDepartamentoId()));

        Funcionario func = new Funcionario();
        func.setCpf(limparCPF(dto.getCpf()));
        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setCargo(dto.getCargo());
        func.setDepartamento(depto);
        func.setPerfil(Perfil.FUNCIONARIO); // Regra de negócio

        // @PrePersist cuidará de xp, nivel, status e dataCadastro
        return funcionarioRepository.save(func);
    }


    @Transactional
    public Map<String, Object> cadastrarFuncionario(UsuarioCadastroDTO dto) {
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
        func.setPerfil(dto.getPerfil()); // Perfil definido pelo Admin

        Funcionario novoFuncionario = funcionarioRepository.save(func);

        // Retorna um Map, como no seu exemplo
        return Map.of(
                "id", novoFuncionario.getId(),
                "nome", novoFuncionario.getNome(),
                "email", novoFuncionario.getEmail(),
                "perfil", novoFuncionario.getPerfil(),
                "mensagem", "Usuário " + novoFuncionario.getPerfil() + " criado com sucesso."
        );
    }

    /**
     * R18: Listar Usuários (Admin).
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarFuncionarios() {
        return funcionarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new) // Construtor de UsuarioResponseDTO(Funcionario)
                .collect(Collectors.toList());
    }

    /**
     * R18: Buscar Usuário por ID (Admin).
     */
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarFuncionarioPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));
        return new UsuarioResponseDTO(funcionario);
    }

    /**
     * R18: Atualizar Usuário (Admin).
     */
    @Transactional
    public UsuarioResponseDTO atualizarFuncionario(Long id, UsuarioUpdateDTO updateDTO) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        // Atualiza apenas os campos não nulos do DTO
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

    /**
     * R18: Inativar Usuário (Admin).
     */
    @Transactional
    public void inativarFuncionario(Long id) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        func.setStatus(false); // Inativação lógica
        funcionarioRepository.save(func);
    }
}