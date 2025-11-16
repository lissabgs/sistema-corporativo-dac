const express = require('express');
const { createProxyMiddleware, fixRequestBody } = require('http-proxy-middleware');
const app = express();
const port = 8080; // Porta principal do Gateway

// 1. Importe o middleware de autenticação (vamos criá-lo a seguir)
const authMiddleware = require('./middleware/auth');

app.use(express.json());

// Rota pública para o front-end (ex: /login)
app.use('/auth', createProxyMiddleware({
    target: 'http://ms-autenticacao:8081', // Nome do serviço no docker-compose
    changeOrigin: true,
    pathRewrite: { '^/auth': '/' }, // Remove /auth da URL
    onProxyReq: fixRequestBody, // Corrige o body do POST
}));

// Rota para usuários, que precisa de lógica
app.use('/usuarios', (req, res, next) => {
    // Rota /autocadastro é PÚBLICA, deixamos passar
    if (req.path === '/autocadastro' && req.method === 'POST') {
        return next(); // Pula a verificação de token
    }

    // Todas as outras rotas de /usuarios são PROTEGIDAS
    return authMiddleware(req, res, next);
},
createProxyMiddleware({
    target: 'http://ms-usuarios:8082', // Nome do serviço no docker-compose
    changeOrigin: true,

    // --- ESTA É A MUDANÇA ---
    // Remove /usuarios da URL e substitui por /api/funcionarios
    pathRewrite: { '^/usuarios': '/api/funcionarios' },

    onProxyReq: fixRequestBody,
}));

/*
// Exemplo de como adicionar o MS-CURSOS (100% protegido)
app.use('/cursos', authMiddleware, createProxyMiddleware({
    target: 'http://ms-cursos:8083',
    changeOrigin: true,
    pathRewrite: { '^/cursos': '/' },
    onProxyReq: fixRequestBody,
}));
*/

app.listen(port, () => {
    console.log(`API Gateway rodando na porta ${port}`);
});