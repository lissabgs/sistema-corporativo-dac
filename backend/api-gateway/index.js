const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const jwt = require('jsonwebtoken');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

// Simple auth middleware (stub) - in prod use ms-autenticacao to validate
function authMiddleware(req, res, next) {
    const auth = req.headers['authorization'];
    if (!auth) return res.status(401).json({error: 'No token'});
    const token = auth.split(' ')[1];
    try {
        jwt.verify(token, 'secret');
        next();
    } catch (e) {
        return res.status(401).json({error: 'Invalid token'});
    }
}

app.use('/auth', createProxyMiddleware({ target: 'http://ms-autenticacao:8081', changeOrigin: true }));
app.use('/usuarios', createProxyMiddleware({ target: 'http://ms-usuarios:8082', changeOrigin: true, pathRewrite: {'^/usuarios' : ''}}));
app.use('/cursos', createProxyMiddleware({ target: 'http://ms-cursos:8083', changeOrigin: true }));
app.use('/avaliacoes', createProxyMiddleware({ target: 'http://ms-avaliacoes:8084', changeOrigin: true }));
app.use('/progresso', authMiddleware, createProxyMiddleware({ target: 'http://ms-progresso:8085', changeOrigin: true }));
app.use('/gamificacao', authMiddleware, createProxyMiddleware({ target: 'http://ms-gamificacao:8086', changeOrigin: true }));
app.use('/notificacoes', authMiddleware, createProxyMiddleware({ target: 'http://ms-notificacoes:8087', changeOrigin: true }));

app.get('/', (req, res) => res.json({ status: 'API Gateway', services: Object.keys(require('./package.json').dependencies) }));

const port = process.env.PORT || 8080;
app.listen(port, () => console.log('API Gateway running on', port));
