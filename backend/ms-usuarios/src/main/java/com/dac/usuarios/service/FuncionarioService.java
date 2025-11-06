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
import com.dac.usuarios.dto.AuthRegistroDTO; // Importar
import com.dac.usuarios.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate; // Importar

import java.util.List;
import java.util.Map;
import java.util.Random; // Importar
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    // Injetar o RestTemplate
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
     * Agora retorna um Map com o funcionário e a senha (para fins de teste)
     */
    @Transactional // Importante para rollback
    public Map<String, Object> autocadastro(FuncionarioAutocadastroDTO dto) {
        Departamento depto = departamentoRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com ID: " + dto.getDepartamentoId()));

        Funcionario func = new Funcionario();
        func.setCpf(limparCPF(dto.getCpf()));
        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setCargo(dto.getCargo());
        func.setDepartamento(depto);
        func.setPerfil(Perfil.FUNCIONARIO);

        Funcionario novoFuncionario = funcionarioRepository.save(func);

        // ---- INÍCIO DA COMUNICAÇÃO ----
        String senhaNumerica = gerarSenhaNumerica();
        AuthRegistroDTO authDTO = new AuthRegistroDTO(
                novoFuncionario.getEmail(),
                senhaNumerica,
                novoFuncionario.getPerfil(),
                novoFuncionario.getId()
        );

        try {
            // Chama o ms-autenticacao usando o nome do serviço do Docker
            // A porta 8081 é a porta interna do contêiner ms-autenticacao
            restTemplate.postForObject("http://ms-autenticacao:8081/api/auth/internal/register", authDTO, Void.class);
        } catch (Exception e) {
            // Se a autenticação falhar, desfaz o cadastro do usuário (rollback)
            throw new RuntimeException("Falha ao registrar usuário no serviço de autenticação. Rollback realizado.", e);
        }
        // ---- FIM DA COMUNICAÇÃO ----

        // Retorna o funcionário e a senha para o controlador
        return Map.of("funcionario", new UsuarioResponseDTO(novoFuncionario), "senhaTemporaria", senhaNumerica);
    }

    /**
     * R18: Cadastro de Usuário (Admin).
     */
    @Transactional // Importante para rollback
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
        func.setPerfil(dto.getPerfil());

        Funcionario novoFuncionario = funcionarioRepository.save(func);

        // ---- INÍCIO DA COMUNICAÇÃO ----
        String senhaNumerica = gerarSenhaNumerica();
        AuthRegistroDTO authDTO = new AuthRegistroDTO(
                novoFuncionario.getEmail(),
                senhaNumerica,
                novoFuncionario.getPerfil(),
                novoFuncionario.getId()
        );

        try {
            restTemplate.postForObject("http://ms-autenticacao:8081/api/auth/internal/register", authDTO, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao registrar usuário no serviço de autenticação. Rollback realizado.", e);
        }
        // ---- FIM DA COMUNICAÇÃO ----

        // Retorna o funcionário e a senha
        return Map.of(
                "funcionario", new UsuarioResponseDTO(novoFuncionario),
                "senhaTemporaria", senhaNumerica,
                "mensagem", "Usuário " + novoFuncionario.getPerfil() + " criado com sucesso."
        );
    }

    // ... (O resto dos métodos: listar, buscar, atualizar, inativar NÃO precisam de mudança) ...

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