import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DepartamentoService {
  private apiUrl = 'http://localhost:8080/api/departamentos';

  constructor(private http: HttpClient) {}

  listarDepartamentos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  criarDepartamento(departamento: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, departamento);
  }

  atualizarDepartamento(id: number, departamento: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, departamento);
  }

  deletarDepartamento(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}