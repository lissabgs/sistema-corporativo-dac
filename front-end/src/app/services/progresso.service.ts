import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProgressoService {
  // Ajuste a URL conforme seu Gateway
  private apiUrl = 'http://localhost:8080/api/progresso'; 

  constructor(private http: HttpClient) { }

  // Método para matricular o aluno
  matricular(funcionarioId: number, cursoId: string): Observable<any> {
    // Envia como Query Params ou Body, dependendo do seu Controller Java.
    // Assumindo POST com Body:
    return this.http.post(`${this.apiUrl}/matricular`, { funcionarioId, cursoId });
  }

  listarMeusCursos(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/meus-cursos/${funcionarioId}`);
  }
}