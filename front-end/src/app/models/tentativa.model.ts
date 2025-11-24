export interface Tentativa {
  id: number;
  funcionarioId: number;
  avaliacaoId: number;
  nota: number;
  dataTentativa: string;
  status: string; // ex: 'PENDENTE', 'CORRIGIDA'
  // Campos auxiliares para exibição
  funcionarioNome?: string;
}