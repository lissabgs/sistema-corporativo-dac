package com.dac.avaliacoes.model;

public enum StatusTentativa {
    PENDENTE,
    EM_ANDAMENTO,
    EM_PROGRESSO,       // <-- Adicionado para corrigir o erro
    CONCLUIDA,
    APROVADO,
    REPROVADO,
    EM_ANALISE,
    AGUARDANDO_CORRECAO,
    CORRIGIDA           // <-- Adicionado para corrigir o erro
}