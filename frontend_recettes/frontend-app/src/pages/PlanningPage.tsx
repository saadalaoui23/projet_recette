import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios.config';
import Header from '../component/Header';

// ✅ Interface correspondant au DTO Java MenuResponse
interface Repas {
  moment: string;     // "DEJEUNER" ou "DINER"
  recetteId: number;
}

interface Menu {
  id: string;
  nom: string;        // Backend envoie "nom", pas "name"
  // Map des jours (MONDAY, TUESDAY...) vers une liste de repas
  planningHebdomadaire: Record<string, Repas[]>;
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

    // ✅ URL CORRIGÉE : /planning/menus
    // La Gateway intercepte "/planning" et envoie "/menus" au microservice
    axiosInstance.get('/planning/menus')
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

  const joursOrdre = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];
    const traduction: Record<string, string> = {
      MONDAY: "Lundi", TUESDAY: "Mardi", WEDNESDAY: "Mercredi",
      THURSDAY: "Jeudi", FRIDAY: "Vendredi", SATURDAY: "Samedi", SUNDAY: "Dimanche"
    };

    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="max-w-full mx-auto px-6 py-10">
          <h2 className="text-3xl font-extrabold text-gray-900 mb-8">Mon Planning Hebdomadaire</h2>

          {menus.map(menu => (
            <div key={menu.id} className="bg-white rounded-2xl shadow-sm p-6 mb-8 border border-gray-100">
              <h3 className="text-2xl font-bold text-blue-600 mb-6 border-b pb-2">{menu.nom}</h3>

              <div className="grid grid-cols-1 md:grid-cols-7 gap-4">
                {joursOrdre.map(jour => {
                  const repasDuJour = menu.planningHebdomadaire[jour];
                  return (
                    <div key={jour} className={`p-4 rounded-xl border ${repasDuJour ? 'bg-blue-50 border-blue-100' : 'bg-gray-50 border-gray-100 opacity-50'}`}>
                      <p className="font-bold text-gray-800 mb-3 text-center">{traduction[jour]}</p>

                      {repasDuJour ? repasDuJour.map((r, idx) => (
                        <div key={idx} className="bg-white p-2 rounded shadow-sm mb-2 text-xs">
                          <p className="text-blue-500 font-bold uppercase text-[10px]">{r.moment}</p>
                          <p className="text-gray-700 font-medium">Recette #{r.recetteId}</p>
                        </div>
                      )) : (
                        <p className="text-[10px] text-gray-400 text-center italic">Aucun repas</p>
                      )}
                    </div>
                  );
                })}
              </div>
            </div>
          ))}
        </main>
      </div>
    );
  };

export default PlanningPage;