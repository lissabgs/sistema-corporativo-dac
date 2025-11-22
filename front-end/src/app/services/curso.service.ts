import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Curso } from '../models/curso.model';

@Injectable({
  providedIn: 'root'
})
export class CursoService {
  // O API Gateway já redireciona /api/cursos para o ms-cursos
  private apiUrl = 'http://localhost:8080/api/cursos';

  constructor(private http: HttpClient) {}

  // NOVO: Busca todos os cursos do banco
  listarCursos(): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.apiUrl);
  }

  // (Seus outros métodos continuam aqui: listarPorInstrutor, criar, etc...)
  listarPorInstrutor(instrutorId: number): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/instrutor/${instrutorId}`);
  }
  
  // ... criar, atualizar, deletar ...
  criar(curso: Curso): Observable<Curso> {
    return this.http.post<Curso>(this.apiUrl, curso);
  }

  atualizar(id: number, curso: Curso): Observable<Curso> {
    return this.http.put<Curso>(`${this.apiUrl}/${id}`, curso);
  }
}