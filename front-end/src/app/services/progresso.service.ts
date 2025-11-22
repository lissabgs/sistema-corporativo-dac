import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http'; // <--- Importe HttpParams
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProgressoService {
  
  private apiUrl = 'http://localhost:8080/api/progresso';

  constructor(private http: HttpClient) {}

  // Busca todos os cursos em que o aluno está matriculado
  listarInscricoes(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/funcionario/${funcionarioId}`);
  }

  // NOVO: Método para realizar a matrícula de fato
  matricular(funcionarioId: number, cursoId: string): Observable<any> {
    // Como o backend espera @RequestParam, usamos HttpParams ou query string
    const params = new HttpParams()
      .set('funcionarioId', funcionarioId.toString())
      .set('cursoId', cursoId);

    return this.http.post(`${this.apiUrl}/matricular`, null, { params });
  }
}