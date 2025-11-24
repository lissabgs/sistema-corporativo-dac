const express = require('express');
const { createProxyMiddleware, fixRequestBody } = require('http-proxy-middleware');
const cors = require('cors');
const app = express();
const port = 8080; // Porta do Gateway

// 1. Middleware de Autenticação
const authMiddleware = require('./middleware/auth');

// 2. Configuração Global (CORS e JSON)
app.use(cors());
app.use(express.json());

// 3. Endereços dos Microsserviços (Docker)
const MS_AUTENTICACAO = 'http://ms-autenticacao:8081';
const MS_USUARIOS     = 'http://ms-usuarios:8082';
const MS_CURSOS       = 'http://ms-cursos:8083';
const MS_AVALIACOES   = 'http://ms-avaliacoes:8084';
const MS_PROGRESSO    = 'http://ms-progresso:8085';
// const MS_GAMIFICACAO= 'http://ms-gamificacao:8086';
// const MS_NOTIFICACOES='http://ms-notificacoes:8087';

// ===================================================================
// ROTAS PÚBLICAS (Sem Auth)
// ===================================================================

// Login (Auth)
app.use('/api/auth', createProxyMiddleware({
    target: MS_AUTENTICACAO,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// Autocadastro de Funcionário (Usuários)
app.use('/api/funcionarios/autocadastro', createProxyMiddleware({
    target: MS_USUARIOS,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// ===================================================================
// ROTAS MISTAS (GET Público / Outros Protegidos)
// ===================================================================

// Departamentos
app.use('/api/departamentos', (req, res, next) => {
    if (req.method === 'GET') {
        return next();
    }
    return authMiddleware(req, res, next);
}, createProxyMiddleware({
    target: MS_USUARIOS,
    changeOrigin: true,
    pathRewrite: { '^/usuarios': '/api/funcionarios' },
    onProxyReq: fixRequestBody,
}));

// Notificações
app.use('/notificacoes', (req, res, next) => {
    return authMiddleware(req, res, next);
}, createProxyMiddleware({
    target: 'http://ms-notificacoes:8087',
    changeOrigin: true,
    pathRewrite: { '^/notificacoes': '/api/notificacoes' },
    onProxyReq: fixRequestBody,
}));

// ===================================================================
// ROTAS PROTEGIDAS (Exigem Token)
// ===================================================================

// Funcionários
app.use('/api/funcionarios', authMiddleware, createProxyMiddleware({
    target: MS_USUARIOS,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// Cursos
app.use('/api/cursos', authMiddleware, createProxyMiddleware({
    target: MS_CURSOS,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// Avaliações (ADICIONADO AQUI)
// Avaliações (CORRIGIDO)
app.use('/api/avaliacoes', (req, res, next) => {
    // Permite que a listagem de cursos disponíveis passe sem o authMiddleware se for GET
    // O req.path aqui é relativo, então verifica apenas '/cursos-disponiveis'
    if (req.path === '/cursos-disponiveis' && req.method === 'GET') {
        return next();
    }
    // Para todas as outras rotas (criar, deletar, etc), exige autenticação
    return authMiddleware(req, res, next);
}, createProxyMiddleware({
    target: MS_AVALIACOES,
    changeOrigin: true,
    onProxyReq: fixRequestBody
}));
// Progresso
app.use('/api/progresso', authMiddleware, createProxyMiddleware({
    target: MS_PROGRESSO,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

app.listen(port, () => {
    console.log(`API Gateway rodando na porta ${port}`);
});