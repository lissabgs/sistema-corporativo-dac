import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Correcao } from '../models/correcao.model';

@Injectable({
  providedIn: 'root'
})
export class CorrecaoService {
  private apiUrl = 'http://localhost:8080/api/correcoes';

  constructor(private http: HttpClient) { }

  corrigirQuestao(tentativaId: number, instrutorId: number, feedback: string, nota: number): Observable<Correcao> {
    const params = {
      tentativaId: tentativaId.toString(),
      instrutorId: instrutorId.toString(),
      feedback: feedback,
      nota: nota.toString()
    };
    // Post com query params conforme seu Controller
    return this.http.post<Correcao>(this.apiUrl, null, { params });
  }

  listarMinhasCorrecoes(instrutorId: number): Observable<Correcao[]> {
    return this.http.get<Correcao[]>(`${this.apiUrl}/minhas-correcoes?instrutorId=${instrutorId}`);
  }
}