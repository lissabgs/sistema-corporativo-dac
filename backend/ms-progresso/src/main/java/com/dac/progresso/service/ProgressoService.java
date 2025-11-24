package com.dac.progresso.service;

import com.dac.progresso.client.CursosClient; // <--- NOVO IMPORT
import com.dac.progresso.config.RabbitMQConfig;
import com.dac.progresso.dto.ConcluirAulaRequestDTO;
import com.dac.progresso.dto.CursoIntegrationDTO; // <--- NOVO IMPORT
import com.dac.progresso.model.Progresso;
import com.dac.progresso.model.StatusProgresso;
import com.dac.progresso.repository.ProgressoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressoService {

    @Autowired
    private ProgressoRepository progressoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CursosClient cursosClient; // <--- INJEÇÃO DO CLIENTE

    // ========== MATRICULAR ALUNO ==========
    @Transactional
    public Progresso matricularAluno(Long funcionarioId, String cursoId) {
        Optional<Progresso> existente = progressoRepository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId);

        if (existente.isPresent()) {
            Progresso p = existente.get();
            if (p.getStatus() == StatusProgresso.CANCELADO) {
                p.setStatus(StatusProgresso.INSCRITO);
                return progressoRepository.save(p);
            }
            return p;
        }

        Progresso novoProgresso = new Progresso(funcionarioId, cursoId);
        novoProgresso.setStatus(StatusProgresso.INSCRITO);
        novoProgresso.setDataInicio(LocalDateTime.now());

        return progressoRepository.save(novoProgresso);
    }

    // ========== MÉTODOS DE CONTROLE DE STATUS ==========

    public Progresso iniciarCurso(String progressoId) {
        Progresso progresso = progressoRepository.findById(progressoId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        if (progresso.getStatus() == StatusProgresso.INSCRITO ||
                progresso.getStatus() == StatusProgresso.PAUSADO ||
                progresso.getStatus() == StatusProgresso.CANCELADO) {

            progresso.setStatus(StatusProgresso.EM_ANDAMENTO);
            return progressoRepository.save(progresso);
        }
        return progresso;
    }

    public Progresso pausarCurso(String progressoId) {
        Progresso progresso = progressoRepository.findById(progressoId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        if (progresso.getStatus() == StatusProgresso.EM_ANDAMENTO) {
            progresso.setStatus(StatusProgresso.PAUSADO);
            return progressoRepository.save(progresso);
        }
        return progresso;
    }

    public Progresso cancelarInscricao(String progressoId) {
        Progresso progresso = progressoRepository.findById(progressoId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        progresso.setStatus(StatusProgresso.CANCELADO);
        return progressoRepository.save(progresso);
    }

    // =========================================================

    public List<Progresso> listarInscricoesDoAluno(Long funcionarioId) {
        return progressoRepository.findByFuncionarioId(funcionarioId);
    }

    public List<String> listarCodigosMatriculados(Long funcionarioId) {
        return progressoRepository.findByFuncionarioId(funcionarioId)
                .stream()
                .filter(p -> p.getStatus() != StatusProgresso.CANCELADO)
                .map(Progresso::getCursoId)
                .collect(Collectors.toList());
    }

    // ========== CONCLUIR AULA E VERIFICAR TÉRMINO ==========
    @Transactional
    public Progresso concluirAula(ConcluirAulaRequestDTO dto) {
        Progresso progresso = progressoRepository
                .findByFuncionarioIdAndCursoId(dto.getFuncionarioId(), dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada. Inicie o curso primeiro."));

        // Se estava inscrito, muda para andamento
        if (progresso.getStatus() == StatusProgresso.INSCRITO) {
            progresso.setStatus(StatusProgresso.EM_ANDAMENTO);
        }

        // Adiciona a aula e verifica se é nova para dar XP
        boolean aulaNova = progresso.adicionarAula(dto.getAulaId());

        if (aulaNova) {
            // Envia XP da aula
            rabbitTemplate.convertAndSend(RabbitMQConfig.XP_QUEUE_NAME,
                    new XpMessageDTO(dto.getFuncionarioId(), dto.getXpGanho()));
        }

        // Verifica se terminou o curso inteiro
        verificarConclusaoCurso(progresso, dto.getFuncionarioId());

        return progressoRepository.save(progresso);
    }

    // Lógica Inteligente: Verifica se todas as aulas obrigatórias foram feitas
    private void verificarConclusaoCurso(Progresso progresso, Long funcionarioId) {
        try {
            // Busca a estrutura do curso no MS-CURSOS para saber o que é obrigatório
            Long cursoIdLong = Long.parseLong(progresso.getCursoId());
            CursoIntegrationDTO curso = cursosClient.obterCursoPorId(cursoIdLong);

            if (curso == null) return;

            boolean todasObrigatoriasConcluidas = true;

            // Verifica módulo a módulo
            if (curso.getModulos() != null) {
                for (CursoIntegrationDTO.ModuloDTO modulo : curso.getModulos()) {
                    if (modulo.getAulas() != null) {
                        for (CursoIntegrationDTO.AulaDTO aula : modulo.getAulas()) {
                            // Se aula for obrigatória e NÃO estiver na lista de concluídas do aluno
                            if (aula.isObrigatorio() && !progresso.getAulasConcluidas().contains(aula.getId())) {
                                todasObrigatoriasConcluidas = false;
                                break;
                            }
                        }
                    }
                    if (!todasObrigatoriasConcluidas) break;
                }
            }

            // Se terminou tudo e o status ainda não é CONCLUIDO
            if (todasObrigatoriasConcluidas && progresso.getStatus() != StatusProgresso.CONCLUIDO) {
                progresso.setStatus(StatusProgresso.CONCLUIDO);
                progresso.setDataConclusao(LocalDateTime.now());

                // Envia o XP Bônus de Conclusão
                if (curso.getXpConclusao() > 0) {
                    rabbitTemplate.convertAndSend(RabbitMQConfig.XP_QUEUE_NAME,
                            new XpMessageDTO(funcionarioId, curso.getXpConclusao()));
                }
                System.out.println("Curso " + curso.getId() + " CONCLUÍDO pelo funcionário " + funcionarioId);
            }

        } catch (Exception e) {
            System.err.println("Erro ao verificar conclusão do curso (MS-CURSOS pode estar off): " + e.getMessage());
        }
    }

    public static class XpMessageDTO {
        public Long funcionarioId;
        public int xp;

        public XpMessageDTO(Long funcionarioId, int xp) {
            this.funcionarioId = funcionarioId;
            this.xp = xp;
        }
    }

    public List<String> listarIdsConcluidos(Long funcionarioId) {
        return progressoRepository.findAll().stream()
                .filter(p -> p.getFuncionarioId().equals(funcionarioId))
                .filter(p -> p.getStatus() == StatusProgresso.CONCLUIDO)
                .map(Progresso::getCursoId)
                .collect(Collectors.toList());
    }
}