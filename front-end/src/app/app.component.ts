import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router'; // Importe Router e NavigationEnd
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './components/sidebar/sidebar.component'; // Importe a Sidebar
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] // Vamos ajustar o CSS também
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
      // Esconde em login, cadastro e possivelmente na home vazia se redirecionar
      const hiddenRoutes = ['/login', '/autocadastro'];
      
      // Verifica se a URL atual começa com alguma das rotas ocultas
      this.showSidebar = !hiddenRoutes.some(route => url.includes(route));
    });
  }
}