import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

interface AuthFormProps { mode: 'login' | 'register'; }

const AuthForm: React.FC<AuthFormProps> = ({ mode }) => {
  const isLogin = mode === 'login';
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', password: ''
  });
  const [status, setStatus] = useState({ message: '', error: false });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); // Empêche le rechargement parasite
    if (loading) return;

    setLoading(true);
    // URL exacte correspondant à vos OPEN_API_ENDPOINTS
    const url = `http://localhost:8000/users/api/users/${isLogin ? 'login' : 'register'}`;

    try {
      const response = await axios.post(url, formData);

      if (isLogin && response.data.token) {
        // AUTHENTICATION : Sauvegarde immédiate du token
        localStorage.setItem('token', response.data.token);

        // REDIRECTION DYNAMIQUE : On ne recharge pas la page pour garder l'état React
        navigate('/home');
      } else {
        setStatus({ message: "Succès ! Veuillez vous connecter.", error: false });
        if (!isLogin) setTimeout(() => navigate('/login'), 1500);
      }
    } catch (err: any) {
      const msg = err.response?.status === 403 ? "Forbidden : Problème CORS Gateway" : "Erreur auth";
      setStatus({ message: msg, error: true });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md bg-white p-8 rounded-xl shadow-lg border">
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">{isLogin ? 'Connexion' : 'Inscription'}</h2>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        {!isLogin && (
          <div className="grid grid-cols-2 gap-3">
            <input name="firstName" placeholder="Prénom" onChange={(e) => setFormData({...formData, firstName: e.target.value})} required className="border p-2 rounded" />
            <input name="lastName" placeholder="Nom" onChange={(e) => setFormData({...formData, lastName: e.target.value})} required className="border p-2 rounded" />
          </div>
        )}
        <input name="email" type="email" placeholder="Email" onChange={(e) => setFormData({...formData, email: e.target.value})} required className="border p-2 rounded" />
        <input name="password" type="password" placeholder="Mot de passe" onChange={(e) => setFormData({...formData, password: e.target.value})} required className="border p-2 rounded" />

        <button type="submit" disabled={loading} className={`p-3 rounded text-white font-bold transition-all ${loading ? 'bg-gray-400' : (isLogin ? 'bg-blue-600' : 'bg-green-600')}`}>
          {loading ? 'Traitement...' : (isLogin ? 'Se connecter' : "S'inscrire")}
        </button>
      </form>
      {status.message && <div className={`mt-4 text-center p-2 rounded ${status.error ? 'text-red-600 bg-red-50' : 'text-green-600 bg-green-50'}`}>{status.message}</div>}
    </div>
  );
};

export default AuthForm;