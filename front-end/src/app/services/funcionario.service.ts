import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Funcionario {
  id?: number;
  cpf: string;
  nome: string;
  email: string;
  cargo: string;
  departamentoId: number;
  departamentoNome?: string;
  perfil: 'ADMINISTRADOR' | 'INSTRUTOR' | 'FUNCIONARIO';
  status: boolean;
  senha?: string; // Usado apenas na criação
}

@Injectable({ providedIn: 'root' })
export class FuncionarioService {
  private apiUrl = 'http://localhost:8080/api/funcionarios';

  constructor(private http: HttpClient) {}

  listar(): Observable<Funcionario[]> {
    return this.http.get<Funcionario[]>(this.apiUrl);
  }

  obterPorId(id: number): Observable<Funcionario> {
    return this.http.get<Funcionario>(`${this.apiUrl}/${id}`);
  }

  criar(funcionario: Funcionario): Observable<any> {
    return this.http.post(this.apiUrl, funcionario);
  }

  atualizar(id: number, funcionario: Partial<Funcionario>): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, funcionario);
  }

  inativar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
