import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CorrecaoService {

  // URL do API Gateway apontando para o microsserviço de avaliações
  private apiUrl = 'http://localhost:8080/ms-avaliacoes/api';

  constructor(private http: HttpClient) { }

  /**
   * Busca os detalhes de uma tentativa específica para correção.
   * Deve retornar as questões, as respostas do aluno e os dados da prova.
   */
  getTentativaParaCorrecao(tentativaId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/tentativas/${tentativaId}/detalhes`);
  }

  /**
   * Envia o objeto de correção com as notas e feedbacks atribuídos pelo instrutor.
   */
  enviarCorrecao(correcaoData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/correcoes`, correcaoData);
  }
}