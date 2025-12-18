import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axiosInstance from '../api/axios.config';

interface Repas {
  moment: string;
  recetteId: number;
}

const PlanningWidget: React.FC = () => {
  const [todayMeals, setTodayMeals] = useState<Repas[]>([]);
  const [loading, setLoading] = useState(true);

  // Fonction pour obtenir le jour actuel en Anglais (comme le backend)
  const getTodayKey = () => {
    const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    return days[new Date().getDay()];
  };

  useEffect(() => {
    // Récupérer le dernier menu créé
    axiosInstance.get('/planning/menus')
      .then(res => {
        const menus = res.data;
        if (menus && menus.length > 0) {
          // On prend le dernier menu ajouté pour l'exemple
          const currentMenu = menus[menus.length - 1];
          const todayKey = getTodayKey();
          // On récupère les repas du jour (s'il y en a)
          const mealsForToday = currentMenu.planningHebdomadaire[todayKey] || [];
          setTodayMeals(mealsForToday);
        }
      })
      .catch(err => console.error("Erreur widget planning", err))
      .finally(() => setLoading(false));
  }, []);

  // Helpers pour l'affichage
  const getMealByMoment = (moment: string) => {
    const meal = todayMeals.find(m => m.moment === moment);
    return meal ? `Recette n°${meal.recetteId}` : "Rien de prévu";
  };

  return (
    <div className="bg-blue-600 rounded-2xl p-6 text-white shadow-lg h-full flex flex-col justify-between">
      <div>
        <h3 className="text-xl font-bold mb-4">Aujourd'hui ({getTodayKey()})</h3>

        {loading ? (
          <p className="text-sm opacity-70">Chargement...</p>
        ) : (
          <>
            <div className="bg-white/10 rounded-xl p-4 border border-white/20 mb-4">
              <p className="text-xs uppercase tracking-wider opacity-70 mb-1">Déjeuner</p>
              <p className="font-semibold text-lg">{getMealByMoment('DEJEUNER')}</p>
            </div>
            <div className="bg-white/10 rounded-xl p-4 border border-white/20">
              <p className="text-xs uppercase tracking-wider opacity-70 mb-1">Dîner</p>
              <p className="font-semibold text-lg">{getMealByMoment('DINER')}</p>
            </div>
          </>
        )}
      </div>

      <Link to="/planning" className="block mt-6 text-center bg-white text-blue-600 py-2 rounded-lg font-bold hover:bg-blue-50 transition">
        Gérer ma semaine
      </Link>
    </div>
  );
};

export default PlanningWidget;