import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios.config';
import Header from '../component/Header';

interface Menu {
  id: string;
  name: string;
  description?: string;
}

const PlanningPage: React.FC = () => {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }

    axiosInstance.get('/planning/api/planning/menus')
      .then(res => setMenus(res.data))
      .catch(err => {
        console.error('Erreur chargement planning:', err);
        if (err.response?.status === 401) {
          localStorage.removeItem('token');
          navigate('/login');
        } else {
          setError("Impossible de charger le planning.");
        }
      })
      .finally(() => setLoading(false));
  }, [navigate]);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="max-w-5xl mx-auto px-6 py-10">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-3xl font-extrabold text-gray-900">Mon planning de repas</h2>
        </div>

        {loading ? (
          <p className="text-gray-500">⏳ Chargement...</p>
        ) : error ? (
          <p className="text-red-600">{error}</p>
        ) : menus.length === 0 ? (
          <p className="text-gray-500">Aucun menu planifié pour le moment.</p>
        ) : (
          <div className="space-y-4">
            {menus.map(m => (
              <div
                key={m.id}
                className="bg-white border border-gray-100 p-4 rounded-xl shadow-sm hover:shadow-md transition"
              >
                <h3 className="font-bold text-lg text-gray-800">{m.name}</h3>
                {m.description && (
                  <p className="text-gray-500 text-sm mt-1">{m.description}</p>
                )}
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
};

export default PlanningPage;


