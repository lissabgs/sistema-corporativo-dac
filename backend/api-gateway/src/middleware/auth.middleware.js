import jwt from 'jsonwebtoken';
import { serviceConfigs, routes } from '../config/gateway.config.js';

const JWT_SECRET = process.env.JWT_SECRET || 'chave_secreta_compartilhada_2025';

export const authMiddleware = (req, res, next) => {
  // Verificar se a rota é pública
  const isPublicRoute = serviceConfigs.auth.publicRoutes.some(publicRoute => 
    req.path.startsWith(publicRoute)
  );
  
  if (isPublicRoute) {
    return next();
  }

  // Encontrar a rota atual
  const currentRoute = routes.find(route => req.path.startsWith(`/api${route.prefix}`));
  
  // Permitir acesso sem auth para mocks
  if (currentRoute && serviceConfigs[currentRoute.service]?.mock) {
    return next();
  }

  // Verificar token JWT para rotas protegidas
  const authHeader = req.headers.authorization;
  
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Token não fornecido' });
  }

  const token = authHeader.split(' ')[1];

  try {
    const decoded = jwt.verify(token, JWT_SECRET);
    req.user = decoded;
    next();
  } catch (error) {
    return res.status(401).json({ error: 'Token inválido ou expirado' });
  }
};