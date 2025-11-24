import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProgressoService {
  // Ajuste a URL conforme o seu Gateway ou API direta
  private apiUrl = 'http://localhost:8080/api/progresso'; 

  constructor(private http: HttpClient) { }

  matricular(funcionarioId: number, cursoId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/matricular`, { funcionarioId, cursoId });
  }

  // CORREÇÃO: A rota no controller é /funcionario/{id}
  listarMeusCursos(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/funcionario/${funcionarioId}`);
  }
}