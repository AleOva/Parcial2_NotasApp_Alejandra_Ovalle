const express = require('express');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const cors = require('cors');
const Database = require('better-sqlite3');

const app = express();
app.use(express.json());
app.use(cors()); // Permite que tu app Android se conecte

const SECRET_KEY = "mi_clave_secreta_super_segura_123";

// --- CONFIGURACIÓN DE LA BASE DE DATOS SQLITE ---
const db = new Database('notas.db');

// Crear tablas si no existen
db.exec(`
  CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE,
    password TEXT,
    nombre TEXT
  );
  CREATE TABLE IF NOT EXISTS notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    title TEXT,
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id)
  );
`);

// --- MIDDLEWARE DE AUTENTICACIÓN (Valida el Token en cada petición) ---
const verificarToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // Formato: "Bearer TOKEN"

    if (!token) return res.status(401).json({ error: 'Token no proporcionado' });

    jwt.verify(token, SECRET_KEY, (err, user) => {
        if (err) return res.status(403).json({ error: 'Token inválido o expirado' });
        req.user = user; // Guardamos los datos del usuario en la petición
        next();
    });
};

// --- ENDPOINTS ---

// 1. LOGIN (Público)
app.post('/api/login', (req, res) => {
    const { email, password } = req.body;

    const user = db.prepare('SELECT * FROM users WHERE email = ?').get(email);
    if (!user) return res.status(401).json({ error: 'Credenciales incorrectas' });

    const passwordValido = bcrypt.compareSync(password, user.password);
    if (!passwordValido) return res.status(401).json({ error: 'Credenciales incorrectas' });

    // Generar Token JWT (válido por 24 horas)
    const token = jwt.sign({ id: user.id, email: user.email }, SECRET_KEY, { expiresIn: '24h' });
    
    res.json({ token, nombre: user.nombre });
});

// 2. REGISTRO (Público, para que puedas crear usuarios de prueba)
app.post('/api/register', (req, res) => {
    const { email, password, nombre } = req.body;
    const hash = bcrypt.hashSync(password, 10);
    try {
        db.prepare('INSERT INTO users (email, password, nombre) VALUES (?, ?, ?)').run(email, hash, nombre);
        res.status(201).json({ message: 'Usuario registrado exitosamente' });
    } catch (e) {
        res.status(400).json({ error: 'El correo ya está registrado' });
    }
});

// 3. OBTENER NOTAS (Protegido, requiere Token)
app.get('/api/notes', verificarToken, (req, res) => {
    // Solo devuelve las notas del usuario que hizo login
    const notes = db.prepare('SELECT * FROM notes WHERE user_id = ?').all(req.user.id);
    res.json(notes);
});

// 4. CREAR NOTA (Protegido, requiere Token)
app.post('/api/notes', verificarToken, (req, res) => {
    const { title, content } = req.body;
    const info = db.prepare('INSERT INTO notes (user_id, title, content) VALUES (?, ?, ?)').run(req.user.id, title, content);
    
    // Devolver la nota recién creada
    const nuevaNota = db.prepare('SELECT * FROM notes WHERE id = ?').get(info.lastInsertRowid);
    res.status(201).json(nuevaNota);
});

// --- INICIAR SERVIDOR ---
const PORT = 3000;
app.listen(PORT, '0.0.0.0', () => {
    console.log(`Backend de NotasApp corriendo en la red en el puerto ${PORT}`);
});
