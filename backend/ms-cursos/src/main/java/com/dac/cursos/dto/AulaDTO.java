package com.dac.cursos.dto;

public class AulaDTO {
    private String titulo;
    private String descricao;
    private String urlConteudo;
    private int ordem;

    // Novos campos
    private boolean obrigatorio;
    private int xpModulo;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUrlConteudo() { return urlConteudo; }
    public void setUrlConteudo(String urlConteudo) { this.urlConteudo = urlConteudo; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }

    public boolean isObrigatorio() { return obrigatorio; }
    public void setObrigatorio(boolean obrigatorio) { this.obrigatorio = obrigatorio; }

    public int getXpModulo() { return xpModulo; }
    public void setXpModulo(int xpModulo) { this.xpModulo = xpModulo; }
}