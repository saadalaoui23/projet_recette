import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios.config';
import Header from '../component/Header';
import RecetteCard from '../component/RecetteCard';

interface Recette {
  id: number;
  nom: string; // ✅ Aligné avec le backend (Recettes.java)
  description: string;
  ingredients: string;
}

const RecettesPage: React.FC = () => {
  const [recettes, setRecettes] = useState<Recette[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }

    // ✅ Appel via la Gateway (port 8000)
    // Le RewritePath de la Gateway transformera /api/recettes en /recettes pour le microservice
    axiosInstance.get('/api/recettes')
      .then(res => setRecettes(res.data))
      .catch(err => {
        console.error('Erreur chargement recettes:', err);
        if (err.response?.status === 401) {
          localStorage.removeItem('token');
          navigate('/login');
        } else {
          setError("Impossible de charger les recettes.");
        }
      })
      .finally(() => setLoading(false));
  }, [navigate]);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="max-w-5xl mx-auto px-6 py-10">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-3xl font-extrabold text-gray-900">Toutes les recettes</h2>
        </div>

        {loading ? (
          <p className="text-gray-500">⏳ Chargement...</p>
        ) : error ? (
          <p className="text-red-600">{error}</p>
        ) : recettes.length === 0 ? (
          <p className="text-gray-500">Aucune recette disponible.</p>
        ) : (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {recettes.map(r => (
              <RecetteCard
                key={r.id}
                nom={r.nom} // ✅ CORRECTION : Utilisez 'nom' et non 'titre'
                description={r.description}
              />
            ))}
          </div>
        )}
      </main>
    </div>
  );
};

export default RecettesPage;