import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Avaliacao } from '../models/avaliacao.model'; // Verifique se o model também existe

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {
  private apiUrl = 'http://localhost:8080/api/avaliacoes'; 

  constructor(private http: HttpClient) { }

  listar(): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Avaliacao> {
    return this.http.get<Avaliacao>(`${this.apiUrl}/${id}`);
  }

  criar(avaliacao: Avaliacao): Observable<Avaliacao> {
    return this.http.post<Avaliacao>(this.apiUrl, avaliacao);
  }

  atualizar(id: number, avaliacao: Avaliacao): Observable<Avaliacao> {
    return this.http.put<Avaliacao>(`${this.apiUrl}/${id}`, avaliacao);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
  listarCursosDisponiveis(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/cursos-disponiveis`);
  }

  // 1. Pendentes (Para fazer)
  listarPendentes(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/funcionario/${funcionarioId}/pendentes`);
  }

  // 2. Aguardando (Já fiz, esperando professor)
  listarAguardando(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/funcionario/${funcionarioId}/aguardando`);
  }

  // 3. Concluídas (Histórico de notas)
  listarConcluidas(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/funcionario/${funcionarioId}/concluidas`);
  }
}