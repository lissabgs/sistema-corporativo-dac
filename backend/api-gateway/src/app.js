import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import morgan from 'morgan';
import rateLimit from 'express-rate-limit';
import proxyRoutes from './routes/proxy.routes.js';
import { authMiddleware } from './middleware/auth.middleware.js';
import dotenv from 'dotenv';

dotenv.config();

const app = express();

// Middlewares básicos
app.use(helmet());
app.use(cors());
app.use(morgan('combined'));
app.use(express.json());

// Rate Limiting
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 100
});
app.use(limiter);

// Health Check
app.get('/health', (req, res) => {
  res.json({ 
    status: 'API Gateway running', 
    timestamp: new Date().toISOString(),
    services: {
      auth: 'active',
      usuarios: 'mock',
      cursos: 'mock',
      avaliacoes: 'mock',
      progresso: 'mock',
      gamificacao: 'mock',
      notificacoes: 'mock'
    }
  });
});

// Aplicar autenticação
app.use(authMiddleware);

// Configurações de proxy
app.use('/api', proxyRoutes);

// Rota para listar endpoints disponíveis
app.get('/api/endpoints', (req, res) => {
  res.json({
    available: [
      { path: '/api/auth/*', service: 'MS-AUTH', status: 'active' },
      { path: '/api/usuarios/*', service: 'MS-USUARIOS', status: 'mock' },
      { path: '/api/cursos/*', service: 'MS-CURSOS', status: 'mock' },
      { path: '/api/avaliacoes/*', service: 'MS-AVALIACOES', status: 'mock' },
      { path: '/api/progresso/*', service: 'MS-PROGRESSO', status: 'mock' },
      { path: '/api/gamificacao/*', service: 'MS-GAMIFICACAO', status: 'mock' },
      { path: '/api/notificacoes/*', service: 'MS-NOTIFICACOES', status: 'mock' }
    ]
  });
});

// Rota de fallback
app.use('*', (req, res) => {
  res.status(404).json({ error: 'Rota não encontrada' });
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`API Gateway running on port ${PORT}`);
  console.log(`Health check: http://localhost:${PORT}/health`);
  console.log(`Endpoints: http://localhost:${PORT}/api/endpoints`);
});