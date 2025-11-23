export interface Questao {
  id?: number;
  tipoQuestao: 'OBJETIVA' | 'DISCURSIVA' | 'VERDADEIRO_FALSO'; // Ajuste conforme seu Enum no backend
  enunciado: string;
  opcoesResposta?: string; // Pode ser um JSON stringify ou texto separado por vírgula
  respostaCorreta: string;
  peso: number;
  ordem: number;
}

export interface Avaliacao {
  id?: number;
  codigo: string;
  titulo: string;
  descricao: string;
  cursoId: number;
  tempoLimiteMinutos: number;
  tentativasPermitidas: number;
  notaMinima: number;
  ativo?: boolean;
  questoes: Questao[];
  temTentativas?: boolean;
}