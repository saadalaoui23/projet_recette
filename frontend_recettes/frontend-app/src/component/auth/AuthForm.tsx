import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axios.config'; // Assurez-vous que le chemin est bon

interface AuthFormProps { mode: 'login' | 'register'; }

const AuthForm: React.FC<AuthFormProps> = ({ mode }) => {
  const isLogin = mode === 'login';
  const navigate = useNavigate();

  // 1. États visuels
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState({ message: '', error: false });
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', password: ''
  });

  // 2. Verrouillage physique (empêche le double appel API)
  const isSubmitting = useRef(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Si déjà en cours de soumission, on arrête TOUT de suite
    if (isSubmitting.current) return;

    // On active le verrou
    isSubmitting.current = true;
    setLoading(true);
    setStatus({ message: isLogin ? 'Connexion...' : 'Inscription...', error: false });

    // URL relative (le proxy Vite redirigera vers localhost:8000)
    const url = `/users/api/users/${isLogin ? 'login' : 'register'}`;

    // 3. Filtrage des données (Correction erreur 400)
    // On n'envoie pas firstName/lastName au Login
    const dataToSend = isLogin
      ? { email: formData.email, password: formData.password }
      : formData;

    try {
      const response = await axiosInstance.post(url, dataToSend);

      if (isLogin && response.data.token) {
        // Succès Login
        localStorage.setItem('token', response.data.token);
        navigate('/home');
      } else {
        // Succès Inscription
        setStatus({ message: "Compte créé ! Redirection...", error: false });
        setTimeout(() => navigate('/login'), 1500);
      }
    } catch (err: any) {
      // En cas d'erreur, on libère le verrou pour permettre de réessayer
      isSubmitting.current = false;
      setLoading(false);

      // Gestion des messages d'erreur
      let msg = "Erreur inconnue";
      if (err.response) {
        if (err.response.status === 403) msg = "Accès refusé (403) : Vérifiez la clé JWT";
        else if (err.response.status === 401) msg = "Email ou mot de passe incorrect";
        else if (err.response.status === 400) msg = "Données invalides ou email déjà pris";
        else msg = `Erreur serveur (${err.response.status})`;
      } else {
        msg = "Impossible de joindre le serveur (Vérifiez que la Gateway tourne sur le port 8000)";
      }
      setStatus({ message: msg, error: true });
    }
  };

  return (
    <div className="w-full max-w-md bg-white p-8 rounded-xl shadow-lg border">
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">
        {isLogin ? 'Connexion' : 'Inscription'}
      </h2>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        {!isLogin && (
          <div className="grid grid-cols-2 gap-3">
            <input name="firstName" placeholder="Prénom" onChange={(e) => setFormData({...formData, firstName: e.target.value})} required className="border p-2 rounded" />
            <input name="lastName" placeholder="Nom" onChange={(e) => setFormData({...formData, lastName: e.target.value})} required className="border p-2 rounded" />
          </div>
        )}
        <input name="email" type="email" placeholder="Email" onChange={(e) => setFormData({...formData, email: e.target.value})} required className="border p-2 rounded" />
        <input name="password" type="password" placeholder="Mot de passe" onChange={(e) => setFormData({...formData, password: e.target.value})} required className="border p-2 rounded" />

        <button
          type="submit"
          disabled={loading}
          className={`p-3 rounded text-white font-bold transition-all ${loading ? 'bg-gray-400 cursor-not-allowed' : (isLogin ? 'bg-blue-600' : 'bg-green-600')}`}
        >
          {loading ? 'Traitement...' : (isLogin ? 'Se connecter' : "S'inscrire")}
        </button>
      </form>
      {status.message && <div className={`mt-4 text-center p-2 rounded ${status.error ? 'text-red-600 bg-red-50' : 'text-green-600 bg-green-50'}`}>{status.message}</div>}
    </div>
  );
};

export default AuthForm;