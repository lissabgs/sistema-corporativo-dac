import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

/**
 * Retorna uma função CanActivateFn que verifica se o usuário está logado
 * E se o perfil do usuário é um dos perfis permitidos.
 * @param allowedRoles Lista de strings com os perfis permitidos (ex: 'ADMINISTRADOR', 'INSTRUTOR', 'FUNCIONARIO').
 */
export const roleGuard: (allowedRoles: string[]) => CanActivateFn = 
  (allowedRoles: string[]) => {
    return () => {
      const router = inject(Router);
      // Busca token e perfil (dados salvos no localStorage pelo AuthService)
      const token = localStorage.getItem('authToken');
      const perfil = localStorage.getItem('usuarioPerfil');

      // 1. Verifica se está logado
      if (!token || !perfil) {
        // Não logado: Redireciona para o login
        return router.createUrlTree(['/login']);
      }

      // 2. Verifica se o perfil é permitido
      if (allowedRoles.includes(perfil)) {
        // Perfil permitido
        return true;
      } else {
        // Perfil não permitido: Redireciona para Acesso Negado
        return router.createUrlTree(['/acesso-negado']);
      }
    };
  };