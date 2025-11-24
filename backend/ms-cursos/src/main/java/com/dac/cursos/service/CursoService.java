package com.dac.cursos.service;

import com.dac.cursos.dto.CursoRequestDTO;
import com.dac.cursos.dto.UsuarioDTO; // Importe o DTO novo
import com.dac.cursos.model.Aula;
import com.dac.cursos.model.Curso;
import com.dac.cursos.model.Modulo;
import com.dac.cursos.model.StatusCurso;
import com.dac.cursos.repository.CursoRepository;
import com.dac.cursos.client.ProgressoClient;
import com.dac.cursos.client.UsuariosClient; // Importe o Client novo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProgressoClient progressoClient;

    @Autowired
    private UsuariosClient usuariosClient; // Injeção do Cliente de Usuários

    // ... (Métodos createCurso e atualizarCurso mantêm-se iguais) ...
    @Transactional
    public Curso createCurso(CursoRequestDTO dto) {
        Curso curso = new Curso();
        preencherDadosCurso(curso, dto);
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso atualizarCurso(Long id, CursoRequestDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
        preencherDadosCurso(curso, dto);
        return cursoRepository.save(curso);
    }

    // --- ATUALIZADO PARA BUSCAR O NOME DO INSTRUTOR ---
    public Curso buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));

        carregarNomeInstrutor(curso); // Chama o método auxiliar

        return curso;
    }
    // --------------------------------------------------

    @Transactional
    public void inativarCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
        curso.setStatus(StatusCurso.INATIVO);
        cursoRepository.save(curso);
    }

    @Transactional
    public Curso atualizarStatus(Long id, String novoStatusStr) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));

        StatusCurso novoStatus;
        try {
            novoStatus = StatusCurso.valueOf(novoStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + novoStatusStr);
        }
        curso.setStatus(novoStatus);
        return cursoRepository.save(curso);
    }

    public List<Curso> listarPorInstrutor(Long instrutorId) {
        return cursoRepository.findByInstrutorId(instrutorId);
    }

    public List<Curso> listarTodos() {
        List<Curso> cursos = cursoRepository.findAll();
        // Opcional: carregar nomes para todos (pode ser lento se tiver muitos)
        // cursos.forEach(this::carregarNomeInstrutor);
        return cursos;
    }

    public List<Curso> listarDisponiveisParaAluno(String departamento, String nivelAluno, Long funcionarioId) {
        // ... (Lógica de níveis mantém-se igual) ...
        List<String> niveisPermitidos;
        String nivel = nivelAluno != null ? nivelAluno.toUpperCase() : "INICIANTE";

        switch (nivel) {
            case "AVANCADO":
            case "AVANÇADO":
                niveisPermitidos = Arrays.asList("Iniciante", "Intermediário", "Avançado", "INICIANTE", "INTERMEDIARIO", "AVANCADO");
                break;
            case "INTERMEDIARIO":
            case "INTERMEDIÁRIO":
                niveisPermitidos = Arrays.asList("Iniciante", "Intermediário", "INICIANTE", "INTERMEDIARIO");
                break;
            default:
                niveisPermitidos = Arrays.asList("Iniciante", "INICIANTE");
                break;
        }

        List<Curso> todosCursos = cursoRepository.findAll().stream()
                .filter(c -> c.getStatus() == StatusCurso.ATIVO)
                .filter(c -> departamento == null || (c.getCategoriaId() != null && c.getCategoriaId().equalsIgnoreCase(departamento)))
                .filter(c -> c.getNivelDificuldade() != null && niveisPermitidos.contains(c.getNivelDificuldade()))
                .collect(Collectors.toList());

        // Filtragem de matriculados
        List<String> matriculados = new ArrayList<>();
        try {
            if (funcionarioId != null) {
                matriculados = progressoClient.obterIdsMatriculados(funcionarioId);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar matrículas: " + e.getMessage());
        }

        if (!matriculados.isEmpty()) {
            List<String> finalMatriculados = matriculados;
            todosCursos.removeIf(curso -> {
                String idString = curso.getId().toString();
                String codigo = curso.getCodigo();
                return finalMatriculados.contains(idString) || finalMatriculados.contains(codigo);
            });
        }

        // Carrega o nome do instrutor para os cursos que sobraram na lista
        todosCursos.forEach(this::carregarNomeInstrutor);

        return todosCursos;
    }

    // --- MÉTODO AUXILIAR NOVO ---
    private void carregarNomeInstrutor(Curso curso) {
        if (curso.getInstrutorId() != null) {
            try {
                UsuarioDTO instrutor = usuariosClient.buscarPorId(curso.getInstrutorId());
                if (instrutor != null) {
                    curso.setInstrutorNome(instrutor.getNome());
                }
            } catch (Exception e) {
                // Se der erro (ex: instrutor deletado ou serviço fora), coloca um fallback
                curso.setInstrutorNome("Instrutor (ID " + curso.getInstrutorId() + ")");
                System.err.println("Erro ao buscar nome do instrutor: " + e.getMessage());
            }
        }
    }
    // ----------------------------

    private void preencherDadosCurso(Curso curso, CursoRequestDTO dto) {
        // ... (Mantém a lógica de preencher dados igual ao original) ...
        // Copie o método preencherDadosCurso do seu código anterior para cá
        // para garantir que a criação/edição continue funcionando.
        curso.setCodigo(dto.getCodigo());
        curso.setTitulo(dto.getTitulo());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoriaId(dto.getCategoriaId());
        curso.setInstrutorId(dto.getInstrutorId());
        curso.setDuracaoEstimada(dto.getDuracaoEstimada());
        curso.setNivelDificuldade(dto.getNivelDificuldade());

        int xpTotal = dto.getXpOferecido();
        curso.setXpOferecido(xpTotal);
        int xpConclusao = (int) (xpTotal * 0.50);
        curso.setXpConclusao(xpConclusao);
        int xpAvaliacao = (int) (xpTotal * 0.25);
        curso.setXpAvaliacao(xpAvaliacao);
        int xpParaAulas = (int) (xpTotal * 0.25);
        int xpPorAula = 0;

        if (dto.getPreRequisitos() != null) {
            curso.setPreRequisitos(new ArrayList<>(dto.getPreRequisitos()));
        } else {
            curso.setPreRequisitos(new ArrayList<>());
        }

        if (dto.getStatus() != null) {
            try {
                curso.setStatus(StatusCurso.valueOf(dto.getStatus().toUpperCase()));
            } catch (Exception e) {
                curso.setStatus(StatusCurso.RASCUNHO);
            }
        } else if (curso.getStatus() == null) {
            curso.setStatus(StatusCurso.RASCUNHO);
        }

        long totalAulas = 0;
        if (dto.getModulos() != null) {
            totalAulas = dto.getModulos().stream()
                    .filter(m -> m.getAulas() != null)
                    .mapToLong(m -> m.getAulas().size())
                    .sum();
        }

        if (totalAulas > 0) {
            xpPorAula = xpParaAulas / (int) totalAulas;
        }

        if (dto.getModulos() != null) {
            curso.getModulos().clear();
            int finalXpPorAula = xpPorAula;

            List<Modulo> novosModulos = dto.getModulos().stream().map(modDto -> {
                List<Aula> aulas = new ArrayList<>();
                if (modDto.getAulas() != null) {
                    aulas = modDto.getAulas().stream().map(aulaDto -> new Aula(
                            aulaDto.getTitulo(),
                            aulaDto.getDescricao(),
                            aulaDto.getUrlConteudo(),
                            aulaDto.getOrdem(),
                            aulaDto.isObrigatorio(),
                            finalXpPorAula
                    )).collect(Collectors.toList());
                }
                return new Modulo(modDto.getTitulo(), modDto.getOrdem(), modDto.isObrigatorio(), aulas);
            }).collect(Collectors.toList());

            curso.getModulos().addAll(novosModulos);
        }
    }
}