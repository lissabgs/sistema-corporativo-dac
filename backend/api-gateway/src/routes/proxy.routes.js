import express from 'express';
import { createProxyMiddleware } from 'http-proxy-middleware';
import { serviceConfigs, routes } from '../config/gateway.config.js';

const router = express.Router();

// Mock responses para MS não implementados
const mockResponses = {
  '/usuarios': { 
    message: 'MS-USUARIOS em desenvolvimento', 
    status: 'coming_soon',
    documentation: 'Gerencia funcionários, instrutores, departamentos e conquistas'
  },
  '/cursos': { 
    message: 'MS-CURSOS em desenvolvimento', 
    status: 'coming_soon',
    documentation: 'Gerencia categorias, cursos, módulos e materiais'
  },
  '/avaliacoes': { 
    message: 'MS-AVALIACOES em desenvolvimento', 
    status: 'coming_soon',
    documentation: 'Gerencia avaliações, questões e tentativas'
  },
  '/progresso': { 
    message: 'MS-PROGRESSO em desenvolvimento', 
    status: 'coming_soon',
    documentation: 'Gerencia inscrições, progresso e certificados'
  },
  '/gamificacao': { 
    message: 'MS-GAMIFICACAO em desenvolvimento', 
    status: 'coming_soon',
    documentation: 'Gerencia badges, ranking e sistema de XP'
  },
  '/notificacoes': { 
    message: 'MS-NOTIFICACOES em desenvolvimento', 
    status: 'coming_soon',
    documentation: 'Gerencia notificações e filas de email'
  }
};

routes.forEach(route => {
  const serviceConfig = serviceConfigs[route.service];
  
  if (serviceConfig.mock) {
    // Rota mock para MS não implementados
    router.all(`${route.prefix}/*`, (req, res) => {
      const mockResponse = mockResponses[route.prefix] || { 
        message: 'Serviço em desenvolvimento' 
      };
      res.status(503).json(mockResponse);
    });
  } else {
    // Proxy real para MS implementados
    const proxyOptions = {
      target: serviceConfig.target,
      changeOrigin: true,
      pathRewrite: {
        [`^/api${route.prefix}`]: `${route.prefix}`,
      },
      onError: (err, req, res) => {
        console.error(`Erro no proxy para ${route.service}:`, err);
        res.status(502).json({ 
          error: `Serviço ${route.service} indisponível`,
          details: err.message 
        });
      },
      timeout: 30000
    };
    
    router.use(`${route.prefix}`, createProxyMiddleware(proxyOptions));
  }
});

export default router;