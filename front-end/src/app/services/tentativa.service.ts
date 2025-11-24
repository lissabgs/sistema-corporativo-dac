import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tentativa } from '../models/tentativa.model';

@Injectable({
  providedIn: 'root'
})
export class TentativaService {
  private apiUrl = 'http://localhost:8080/api/tentativas';

  constructor(private http: HttpClient) { }

  listarPorAvaliacao(avaliacaoId: number): Observable<Tentativa[]> {
    return this.http.get<Tentativa[]>(`${this.apiUrl}/avaliacao/${avaliacaoId}`);
  }
}