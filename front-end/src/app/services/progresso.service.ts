import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProgressoService {
  // Aponta para o Gateway na porta 8080
  private apiUrl = 'http://localhost:8080/api/progresso'; 

  constructor(private http: HttpClient) { }

  matricular(funcionarioId: number, cursoId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/matricular`, { funcionarioId, cursoId });
  }

  // ATUALIZADO: Agora chama a rota /meus-cursos
  listarMeusCursos(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/meus-cursos/${funcionarioId}`);
  }
}