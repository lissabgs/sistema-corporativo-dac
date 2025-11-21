const jwt = require('jsonwebtoken');

// 1. Pegue o SEGREDO EXATO do seu ms-autenticacao
const JWT_SECRET = "c2VuaGFTZWNyZXRhRG8tUHJvamV0by1EQUMtMjAyNQ==";
const JWT_SECRET = Buffer.from(JWT_SECRET_STRING, 'base64');

module.exports = (req, res, next) => {
    const authHeader = req.headers.authorization;

    if (!authHeader) {
        return res.status(401).send({ error: 'Nenhum token foi fornecido' });
    }

    // Token: "Bearer <token>"
    const parts = authHeader.split(' ');
    if (parts.length !== 2) {
        return res.status(401).send({ error: 'Token com formato inválido' });
    }

    const [scheme, token] = parts;

    if (!/^Bearer$/i.test(scheme)) {
        return res.status(401).send({ error: 'Token mal formatado' });
    }

    // 2. Verifique o token
    jwt.verify(token, JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(401).send({ error: 'Token inválido ou expirado' });
        }

        // 3. Anexe os dados do usuário na requisição
        req.userEmail = decoded.sub; // 'sub' (subject) é o email
        req.userRole = decoded.role; // 'role' é o tipoUsuario

        return next(); // Deixa a requisição continuar
    });
};