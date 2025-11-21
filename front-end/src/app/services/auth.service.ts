import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginResponse {
  token: string;
  usuarioId: number;
  perfil: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(credentials: { email: string, senha: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('authToken', response.token);
        localStorage.setItem('usuarioId', response.usuarioId.toString());
        localStorage.setItem('usuarioPerfil', response.perfil);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('usuarioId');
    localStorage.removeItem('usuarioPerfil');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }
}