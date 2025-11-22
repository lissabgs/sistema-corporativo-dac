export type StatusCurso = 'RASCUNHO' | 'ATIVO' | 'INATIVO' | 'ARQUIVADO' | 'PAUSADO';

export interface Aula {
  titulo: string;
  descricao: string;
  urlConteudo: string;
  ordem: number;
  obrigatorio: boolean;
  xpModulo: number;
}

export interface Modulo {
  titulo: string;
  ordem: number;
  aulas: Aula[];
}

export interface Curso {
  id?: number;
  codigo: string;
  titulo: string;
  descricao: string;
  categoriaId: string;
  instrutorId: number;
  duracaoEstimada: string;
  xpOferecido: number;
  nivelDificuldade: string;
  status: StatusCurso;
  preRequisitos: string[];
  modulos: Modulo[];
}