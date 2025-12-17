import React, { useState } from 'react';
import axios from 'axios';

const Login: React.FC = () => {
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [status, setStatus] = useState<{ message: string; type: 'error' | 'success' | '' }>({ message: '', type: '' });
  const [token, setToken] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setStatus({ message: 'Tentative de connexion...', type: '' });

    try {
      // On cible directement le port 8083 pour isoler les tests
      const response = await axios.post('http://localhost:8083/api/users/login', credentials);

      if (response.status === 200) {
        setToken(response.data.token);
        setStatus({ message: 'Connexion réussie !', type: 'success' });
        console.log('JWT Token reçu:', response.data.token);
      }
    } catch (error: any) {
      if (error.response && error.response.status === 401) {
        setStatus({ message: '401 : Identifiants incorrects.', type: 'error' });
      } else {
        setStatus({ message: `Erreur : ${error.message}`, type: 'error' });
      }
    }
  };

  return (
    <div className="login-container" style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
      <h2>Connexion</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input
          type="email" name="email" placeholder="Email (saad1@example.com)"
          value={credentials.email} onChange={handleChange} required
        />
        <input
          type="password" name="password" placeholder="Mot de passe"
          value={credentials.password} onChange={handleChange} required
        />
        <button type="submit" style={{ cursor: 'pointer' }}>Se connecter</button>
      </form>

      {status.message && (
        <p style={{ color: status.type === 'error' ? 'red' : 'green' }}>{status.message}</p>
      )}

      {token && (
        <div style={{ marginTop: '20px', wordBreak: 'break-all', backgroundColor: '#f4f4f4', padding: '10px' }}>
          <strong>Token JWT :</strong><br/>{token}
        </div>
      )}
    </div>
  );
};

export default Login;