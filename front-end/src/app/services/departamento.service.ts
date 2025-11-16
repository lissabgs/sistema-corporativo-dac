import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DepartamentoService {
  private apiUrl = 'http://localhost:8080/api/departamentos'; // ajuste o endpoint se necessário

  constructor(private http: HttpClient) {}

  listarDepartamentos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
