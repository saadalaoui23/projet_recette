import axios from 'axios';

// ✅ Configuration Axios globale
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8000', // URL de la Gateway
  timeout: 10000, // Timeout de 10 secondes
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  }
});

// ✅ Intercepteur pour ajouter automatiquement le JWT aux requêtes
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');

    // ✅ Ne PAS envoyer le JWT pour les routes d'authentification publiques
    const url = config.url || '';
    const isAuthEndpoint =
      url.includes('/users/api/users/login') ||
      url.includes('/users/api/users/register');

    if (token && !isAuthEndpoint) {
      config.headers = config.headers || {};
      (config.headers as any).Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Intercepteur pour gérer les erreurs 401 (token expiré)
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expiré ou invalide
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;