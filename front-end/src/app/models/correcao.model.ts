export interface Correcao {
  id?: number;
  tentativaId: number;
  instrutorId: number;
  feedback: string;
  notaParcial: number;
  status?: string;
  dataCriacao?: string;
}

export interface TentativaPendente {
  id: number; // ID da tentativa
  avaliacaoTitulo: string;
  alunoNome: string;
  dataTentativa: string;
}

export interface DashboardInstrutorDTO {
  totalAvaliacoesCriadas: number;
  totalTentativasRealizadas: number;
  tentativasPendentesCorrecao: number;
  mediaNotasGeral: number;
  avaliacoesRecentes: any[];
  tentativasPendentes: TentativaPendente[];
  estatisticasPorAvaliacao: any[];
}