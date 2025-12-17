import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Header from '../component/Header';
import RecetteCard from '../component/RecetteCard';
import PlanningWidget from '../component/WidgetPlanning';

const HomePage: React.FC = () => {
  const [recettes, setRecettes] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }

    // Appel via Gateway avec le header Authorization pour passer JwtAuth
    axios.get('http://localhost:8000/recettes', {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(res => setRecettes(res.data.slice(0, 4)))
    .catch(err => {
      if (err.response?.status === 401) {
        localStorage.removeItem('token'); // Nettoyage si token invalide
        navigate('/login');
      }
    })
    .finally(() => setLoading(false));
  }, [navigate]);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="max-w-6xl mx-auto px-6 py-10">
        <h2 className="text-4xl font-extrabold text-gray-900 mb-10">Tableau de bord 👋</h2>
        <div className="grid lg:grid-cols-3 gap-10">
          <aside className="lg:col-span-1"><PlanningWidget /></aside>
          <section className="lg:col-span-2">
            <h3 className="text-2xl font-bold text-gray-800 mb-6">Recettes suggérées</h3>
            <div className="grid sm:grid-cols-2 gap-6">
              {loading ? <p>Chargement...</p> : recettes.map(r => <RecetteCard key={r.id} titre={r.titre} description={r.description} />)}
            </div>
          </section>
        </div>
      </main>
    </div>
  );
};
export default HomePage;