import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Avaliacao } from '../models/avaliacao.model';

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {

  // APONTAMENTO PARA O API GATEWAY (Porta 8080)
  private gatewayUrl = 'http://localhost:8080/api';
  
  private apiUrl = `${this.gatewayUrl}/avaliacoes`;
  private tentativasUrl = `${this.gatewayUrl}/tentativas`;
  private correcoesUrl = `${this.gatewayUrl}/correcoes`;
  private cursosUrl = `${this.gatewayUrl}/cursos`; // URL direta para o MS-CURSOS

  constructor(private http: HttpClient) { }

  // ========== CRUD DE AVALIAÇÕES ==========

  listar(): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(this.apiUrl);
  }

  listarPorInstrutor(instrutorId: number): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(`${this.apiUrl}/instrutor/${instrutorId}`);
  }

  buscarPorId(id: number): Observable<Avaliacao> {
    return this.http.get<Avaliacao>(`${this.apiUrl}/${id}`);
  }

  criar(avaliacao: any): Observable<Avaliacao> {
    return this.http.post<Avaliacao>(this.apiUrl, avaliacao);
  }

  atualizar(id: number, avaliacao: any): Observable<Avaliacao> {
    return this.http.put<Avaliacao>(`${this.apiUrl}/${id}`, avaliacao);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // ========== AUXILIARES PARA O FORMULÁRIO ==========
  
  /**
   * CORREÇÃO: Busca a lista de cursos diretamente no microsserviço de cursos
   * através do Gateway (/api/cursos).
   * Isso resolve o erro "404 Not Found" ao abrir o modal de Nova Avaliação.
   */
  listarCursosDisponiveis(): Observable<any[]> {
    // Chama GET http://localhost:8080/api/cursos
    // (Certifique-se de que o usuário tem permissão ou o endpoint é acessível)
    return this.http.get<any[]>(this.cursosUrl);
  }

  // ========== CORREÇÃO DE PROVAS (TENTATIVAS) ==========

  /**
   * Busca todas as tentativas de um curso específico.
   * Usado na tela "Prancheta" (ListarAvaliacoesCursoComponent).
   */
  buscarTentativasPorCurso(cursoId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.tentativasUrl}/curso/${cursoId}`);
  }
}