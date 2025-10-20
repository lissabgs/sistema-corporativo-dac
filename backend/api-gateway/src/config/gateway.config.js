export const serviceConfigs = {
  auth: {
    target: process.env.MS_AUTH_URL || 'http://ms-autenticacao:8081',
    publicRoutes: [
      '/api/auth/login',
      '/api/auth/register',
      '/api/auth/validate-email',
      '/api/auth/health'
    ]
  },
  usuarios: {
    target: process.env.MS_USUARIOS_URL || 'http://ms-usuarios:8082',
    mock: true
  },
  cursos: {
    target: process.env.MS_CURSOS_URL || 'http://ms-cursos:8083',
    mock: true
  },
  avaliacoes: {
    target: process.env.MS_AVALIACOES_URL || 'http://ms-avaliacoes:8084',
    mock: true
  },
  progresso: {
    target: process.env.MS_PROGRESSO_URL || 'http://ms-progresso:8085',
    mock: true
  },
  gamificacao: {
    target: process.env.MS_GAMIFICACAO_URL || 'http://ms-gamificacao:8086',
    mock: true
  },
  notificacoes: {
    target: process.env.MS_NOTIFICACOES_URL || 'http://ms-notificacoes:8087',
    mock: true
  }
};

export const routes = [
  { prefix: '/auth', service: 'auth' },
  { prefix: '/usuarios', service: 'usuarios' },
  { prefix: '/cursos', service: 'cursos' },
  { prefix: '/avaliacoes', service: 'avaliacoes' },
  { prefix: '/progresso', service: 'progresso' },
  { prefix: '/gamificacao', service: 'gamificacao' },
  { prefix: '/notificacoes', service: 'notificacoes' }
];