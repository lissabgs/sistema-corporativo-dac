import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { HttpClient } from '@angular/common/http'; // Importar HTTP Client
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatListModule,
    MatIconModule,
    MatDividerModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  userRole: string = '';
  
  // Variáveis para exibir na tela (ficam apenas na memória RAM)
  userName: string = 'Carregando...';
  userCargo: string = '';
  userDept: string = ''; 

  constructor(
    private authService: AuthService, 
    private router: Router,
    private http: HttpClient // Injeção para fazer a requisição
  ) {}

  ngOnInit(): void {
    // Recupera o ID e Perfil salvos no login
    this.userRole = localStorage.getItem('usuarioPerfil') || 'USUARIO';
    const userId = localStorage.getItem('usuarioId');

    if (userId) {
      this.buscarDadosUsuario(userId);
    }
  }

  buscarDadosUsuario(id: string) {
    // Faz a requisição ao Gateway -> MS-USUARIOS
    // Rota definida no FuncionarioController: @GetMapping("/{id}")
    this.http.get<any>(`http://localhost:8080/api/funcionarios/${id}`).subscribe({
      next: (dados) => {
        // Preenche os dados vindos do banco (UsuarioResponseDTO)
        this.userName = dados.nome;
        this.userCargo = dados.cargo;
        this.userDept = dados.departamentoNome; 
      },
      error: (err) => {
        console.error('Erro ao buscar dados do usuário:', err);
        this.userName = 'Usuário';
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}