const express = require('express');
const { createProxyMiddleware, fixRequestBody } = require('http-proxy-middleware');
const cors = require('cors');
const app = express();
const port = 8080;

const authMiddleware = require('./middleware/auth');

app.use(cors());
app.use(express.json());

// Endereços dos Microsserviços
const MS_AUTENTICACAO = 'http://ms-autenticacao:8081';
const MS_USUARIOS     = 'http://ms-usuarios:8082';
const MS_CURSOS       = 'http://ms-cursos:8083';
const MS_AVALIACOES   = 'http://ms-avaliacoes:8084';
const MS_PROGRESSO    = 'http://ms-progresso:8085';
const MS_GAMIFICACAO  = 'http://ms-gamificacao:8086'; // Adicionei caso precise
const MS_NOTIFICACOES = 'http://ms-notificacoes:8087';

// --- ROTAS PÚBLICAS ---
app.use('/api/auth', createProxyMiddleware({
    target: MS_AUTENTICACAO,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

app.use('/api/funcionarios/autocadastro', createProxyMiddleware({
    target: MS_USUARIOS,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// --- ROTAS MISTAS ---
app.use('/api/departamentos', (req, res, next) => {
    if (req.method === 'GET') return next();
    return authMiddleware(req, res, next);
}, createProxyMiddleware({
    target: MS_USUARIOS,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// --- ROTAS PROTEGIDAS ---

// 1. Usuários e Funcionários
app.use('/api/funcionarios', authMiddleware, createProxyMiddleware({
    target: MS_USUARIOS,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// 2. Cursos
app.use('/api/cursos', authMiddleware, createProxyMiddleware({
    target: MS_CURSOS,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// 3. Avaliações (CORRIGIDO E EXPANDIDO)
// O Gateway precisa saber que /api/avaliacoes, /api/tentativas e /api/correcoes vão para o mesmo lugar
app.use('/api/avaliacoes', authMiddleware, createProxyMiddleware({
    target: MS_AVALIACOES,
    changeOrigin: true,
    onProxyReq: fixRequestBody
}));

app.use('/api/tentativas', authMiddleware, createProxyMiddleware({
    target: MS_AVALIACOES,
    changeOrigin: true,
    onProxyReq: fixRequestBody
}));

app.use('/api/correcoes', authMiddleware, createProxyMiddleware({
    target: MS_AVALIACOES,
    changeOrigin: true,
    onProxyReq: fixRequestBody
}));

// 4. Progresso
app.use('/api/progresso', authMiddleware, createProxyMiddleware({
    target: MS_PROGRESSO,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

// 5. Gamificação
app.use('/api/gamificacao', authMiddleware, createProxyMiddleware({
    target: MS_GAMIFICACAO,
    changeOrigin: true,
    onProxyReq: fixRequestBody,
}));

app.listen(port, () => {
    console.log(`API Gateway rodando na porta ${port}`);
});