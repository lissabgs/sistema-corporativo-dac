import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardInstrutorDTO } from '../models/correcao.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardInstrutorService {
  private apiUrl = 'http://localhost:8080/api/dashboard/instrutor';

  constructor(private http: HttpClient) { }

  getDashboard(instrutorId: number): Observable<DashboardInstrutorDTO> {
    return this.http.get<DashboardInstrutorDTO>(`${this.apiUrl}?instrutorId=${instrutorId}`);
  }
}