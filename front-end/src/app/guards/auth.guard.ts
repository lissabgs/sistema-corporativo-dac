import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

/**
 * Retorna uma função CanActivateFn que verifica se o usuário está logado
 * E se o perfil do usuário é um dos perfis permitidos.
 * Protegido contra execução no Servidor (SSR).
 */
export const roleGuard: (allowedRoles: string[]) => CanActivateFn = 
  (allowedRoles: string[]) => {
    return () => {
      const router = inject(Router);
      const platformId = inject(PLATFORM_ID);

      // 1. Verifica se está rodando no NAVEGADOR
      // Se estiver no servidor (SSR), não temos localStorage, então bloqueamos ou redirecionamos.
      if (isPlatformBrowser(platformId)) {
        
        const token = localStorage.getItem('authToken');
        const perfil = localStorage.getItem('usuarioPerfil');

        // 2. Verifica se está logado
        if (!token || !perfil) {
          return router.createUrlTree(['/login']);
        }

        // 3. Verifica se o perfil é permitido
        if (allowedRoles.includes(perfil)) {
          return true;
        } else {
          return router.createUrlTree(['/acesso-negado']);
        }
      
      } else {
        // Se for execução no servidor, retornamos false ou UrlTree para login
        // para evitar erro de "localStorage is not defined".
        return router.createUrlTree(['/login']);
      }
    };
  };