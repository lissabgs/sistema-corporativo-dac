package com.dac.cursos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "cursos") // Define o nome da coleção no MongoDB
public class Curso {

    @Id
    private String id; // ID automático do MongoDB

    // Seus campos
    private String codigo;
    private String titulo;
    private String descricao;
    private String categoriaId; // Como o R04 sugere, categoria é separado
    private Long instrutorId; // ID do Instrutor (vindo do ms-usuarios)
    private String duracaoEstimada; // Ex: "8h" (como no PDF)
    private int xpOferecido;
    private String nivelDificuldade; // Ex: "Iniciante", "Avançado"
    private boolean ativo;
    private List<String> preRequisitos; // Lista de 'codigos' de outros cursos

    // A lista de Aulas (sub-documentos)
    private List<Aula> aulas;

    // Getters e Setters (vou omitir por brevidade, mas você deve adicioná-los
    // ou usar @Getter e @Setter do Lombok)

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }
    public Long getInstrutorId() { return instrutorId; }
    public void setInstrutorId(Long instrutorId) { this.instrutorId = instrutorId; }
    public String getDuracaoEstimada() { return duracaoEstimada; }
    public void setDuracaoEstimada(String duracaoEstimada) { this.duracaoEstimada = duracaoEstimada; }
    public int getXpOferecido() { return xpOferecido; }
    public void setXpOferecido(int xpOferecido) { this.xpOferecido = xpOferecido; }
    public String getNivelDificuldade() { return nivelDificuldade; }
    public void setNivelDificuldade(String nivelDificuldade) { this.nivelDificuldade = nivelDificuldade; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public List<String> getPreRequisitos() { return preRequisitos; }
    public void setPreRequisitos(List<String> preRequisitos) { this.preRequisitos = preRequisitos; }
    public List<Aula> getAulas() { return aulas; }
    public void setAulas(List<Aula> aulas) { this.aulas = aulas; }
}