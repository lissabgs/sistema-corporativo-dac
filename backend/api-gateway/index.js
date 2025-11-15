const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// --- ROTA DE AUTENTICAÇÃO ---
// (Regra antiga que você mencionou)
app.use('/auth', createProxyMiddleware({
    target: 'http://ms-autenticacao:8081',
    pathRewrite: {
        '^/auth': '/'
    }
}));

// --- ROTA DO MS-USUÁRIOS ---
// (Regra nova e flexível que criámos)
app.use('/usuarios', createProxyMiddleware({
    target: 'http://ms-usuarios:8082',
    pathRewrite: {
        '^/usuarios': '/' // Remove /usuarios
    }
}));

// --- ROTA DO MS-CURSOS ---
app.use('/cursos', createProxyMiddleware({
    target: 'http://ms-cursos:8083', // Porta 8083
    pathRewrite: {
        '^/cursos': '/' // Remove /cursos
    }
}));

// --- ROTA DO MS-AVALIAÇÕES ---
app.use('/avaliacoes', createProxyMiddleware({
    target: 'http://ms-avaliacoes:8084', // Porta 8084
    pathRewrite: {
        '^/avaliacoes': '/' // Remove /avaliacoes
    }
}));

// --- ROTA DO MS-PROGRESSO ---
app.use('/progresso', createProxyMiddleware({
    target: 'http://ms-progresso:8085', // Porta 8085 (ESTA ESTAVA ERRADA)
    pathRewrite: {
        '^/progresso': '/' // Remove /progresso
    }
}));

// --- ROTA DO MS-GAMIFICAÇÃO ---
app.use('/gamificacao', createProxyMiddleware({
    target: 'http://ms-gamificacao:8086', // Porta 8086
    pathRewrite: {
        '^/gamificacao': '/' // Remove /gamificacao
    }
}));

// --- ROTA DO MS-NOTIFICAÇÕES ---
app.use('/notificacoes', createProxyMiddleware({
    target: 'http://ms-notificacoes:8087', // Porta 8087
    pathRewrite: {
        '^/notificacoes': '/' // Remove /notificacoes
    }
}));


const PORT = 8080;
app.listen(PORT, () => {
    console.log(`API Gateway rodando na porta ${PORT}`);
});