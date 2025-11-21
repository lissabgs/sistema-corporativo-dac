const jwt = require('jsonwebtoken');

// --- CORREÇÃO AQUI ---
// 1. Usamos a MESMA chave que está no application.yml do ms-autenticacao
// 2. Usamos Buffer.from(..., 'base64') para decodificar, igual o Java faz
const JWT_SECRET_STRING = "bWluaGFTZW5oYVN1cGVyU2VjcmV0YURlMzJCeXRlcyEh";
const JWT_SECRET = Buffer.from(JWT_SECRET_STRING, 'base64');

module.exports = (req, res, next) => {
    const authHeader = req.headers.authorization;

    if (!authHeader) {
        return res.status(401).send({ error: 'Nenhum token foi fornecido' });
    }

    const parts = authHeader.split(' ');
    if (parts.length !== 2) {
        return res.status(401).send({ error: 'Token com formato inválido' });
    }

    const [scheme, token] = parts;

    if (!/^Bearer$/i.test(scheme)) {
        return res.status(401).send({ error: 'Token mal formatado' });
    }

    jwt.verify(token, JWT_SECRET, (err, decoded) => {
        if (err) {
            console.error("Erro na verificação do token:", err.message); // Log para ajudar no debug
            return res.status(401).send({ error: 'Token inválido ou expirado' });
        }

        req.userEmail = decoded.sub;
        req.userRole = decoded.role;

        return next();
    });
};