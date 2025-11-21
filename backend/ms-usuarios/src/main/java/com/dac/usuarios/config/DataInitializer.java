package com.dac.usuarios.config;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.model.Perfil;
import com.dac.usuarios.model.Funcionario;
import com.dac.usuarios.model.Instrutor;
import com.dac.usuarios.repository.DepartamentoRepository;
import com.dac.usuarios.repository.FuncionarioRepository;
import com.dac.usuarios.repository.InstrutorRepository;
import com.dac.usuarios.service.FuncionarioService;
import com.dac.usuarios.dto.UsuarioCadastroDTO;
import com.dac.usuarios.dto.UsuarioResponseDTO; // Importar DTO

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map; // Importar Map
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository; // Necessário para buscar o Funcionario completo

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Override
    public void run(String... args) throws Exception {
        Long tiDepartamentoId = inicializarDepartamentos();

        if (funcionarioRepository.count() == 0) {
            System.out.println(">>> [MS-USUARIOS] Inicializando Usuários Padrão (ADMINISTRADOR e INSTRUTOR)...");

            criarAdminPadrao(tiDepartamentoId);
            criarInstrutorPadrao(tiDepartamentoId);

            System.out.println(">>> [MS-USUARIOS] Usuários Padrão criados. Senhas registradas no MS-AUTENTICACAO.");
        }
    }

    private Long inicializarDepartamentos() {
        if (departamentoRepository.count() == 0) {
            System.out.println(">>> [MS-USUARIOS] Inicializando 5 Departamentos padrão...");

            Departamento ti = criarDepto("TI", "Tecnologia da Informação", "Setor de desenvolvimento e suporte");
            criarDepto("RH", "Recursos Humanos", "Gestão de pessoas e talentos");
            criarDepto("FIN", "Financeiro", "Contabilidade e pagamentos");
            criarDepto("MKT", "Marketing", "Publicidade e propaganda");
            criarDepto("COM", "Comercial", "Vendas e relacionamento com clientes");

            return ti.getId();
        }

        Optional<Departamento> tiOpt = departamentoRepository.findByCodigo("TI");
        return tiOpt.map(Departamento::getId).orElse(1L);
    }

    private Departamento criarDepto(String codigo, String nome, String descricao) {
        Departamento d = new Departamento();
        d.setCodigo(codigo);
        d.setNome(nome);
        d.setDescricao(descricao);
        d.setStatus(true);
        return departamentoRepository.save(d);
    }

    private void criarAdminPadrao(Long departamentoId) {
        UsuarioCadastroDTO adminDto = new UsuarioCadastroDTO();
        adminDto.setCpf("00000000000");
        adminDto.setNome("Administrador Sistema");
        adminDto.setEmail("admin@empresa.com");
        adminDto.setCargo("Gestor de Plataforma");
        adminDto.setDepartamentoId(departamentoId);
        adminDto.setPerfil(Perfil.ADMINISTRADOR);
        adminDto.setSenha("1234");

        funcionarioService.cadastrarFuncionario(adminDto);
    }

    private void criarInstrutorPadrao(Long departamentoId) {
        UsuarioCadastroDTO instrutorDto = new UsuarioCadastroDTO();
        instrutorDto.setCpf("12345678901");
        instrutorDto.setNome("Instrutor de Exemplo");
        instrutorDto.setEmail("instrutor@empresa.com");
        instrutorDto.setCargo("Especialista em Tecnologia");
        instrutorDto.setDepartamentoId(departamentoId);
        instrutorDto.setPerfil(Perfil.INSTRUTOR);
        instrutorDto.setSenha("1234");

        // 1. Executa o cadastro (cria Funcionario e Autenticação)
        Map<String, Object> response = funcionarioService.cadastrarFuncionario(instrutorDto);

        // 2. CORREÇÃO: Pega o DTO de resposta do Map e faz o CAST para o tipo correto (UsuarioResponseDTO)
        UsuarioResponseDTO responseDto = (UsuarioResponseDTO) response.get("funcionario");

        // 3. Busca a entidade Funcionario completa no banco usando o ID do DTO
        // Isso resolve o ClassCastException
        Funcionario funcionario = funcionarioRepository.findById(responseDto.getId())
                .orElseThrow(() -> new RuntimeException("Falha ao recuperar Funcionario recém-criado para Instrutor."));

        // 4. Cria a entidade Instrutor
        Instrutor instrutor = new Instrutor();
        instrutor.setFuncionario(funcionario);
        instrutor.setEspecialidades("Tecnologia");
        instrutor.setBiografia("Instrutor padrão para demonstração.");
        instrutor.setAvaliacaoMedia(4.5);
        instrutor.setStatus(true);

        instrutorRepository.save(instrutor);
    }
}