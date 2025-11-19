import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './components/sidebar/sidebar.component'; // Verifique se este arquivo existe
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'front-end';
  showSidebar = false;

  constructor(private router: Router) {
    // Monitora as mudanças de rota para decidir se mostra ou não a sidebar
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      const url = event.urlAfterRedirects;
      // Esconde em login, cadastro e rotas de acesso negado
      const hiddenRoutes = ['/login', '/autocadastro', '/acesso-negado'];
      
      // Verifica se a URL atual contém alguma das rotas ocultas
      this.showSidebar = !hiddenRoutes.some(route => url.includes(route));
    });
  }
}