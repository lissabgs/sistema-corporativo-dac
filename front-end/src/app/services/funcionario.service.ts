import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FuncionarioService {
  private apiUrl = 'http://localhost:8080/api/funcionarios';

  constructor(private http: HttpClient) {}

  listarFuncionarios(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  criarFuncionario(funcionario: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, funcionario);
  }

  atualizarFuncionario(id: number, funcionario: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, funcionario);
  }

  inativarFuncionario(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}