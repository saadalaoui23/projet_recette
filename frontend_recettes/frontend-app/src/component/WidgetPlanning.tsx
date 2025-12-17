import React from 'react';
import { Link } from 'react-router-dom';

const PlanningWidget: React.FC = () => {
  return (
    <div className="bg-blue-600 rounded-2xl p-6 text-white shadow-lg h-full">
      <h3 className="text-xl font-bold mb-4">Aujourd'hui</h3>
      <div className="bg-white/10 rounded-xl p-4 border border-white/20">
        <p className="text-xs uppercase tracking-wider opacity-70 mb-1">Déjeuner</p>
        <p className="font-semibold text-lg">Pâtes Fraîches</p>
      </div>
      <div className="mt-4 bg-white/10 rounded-xl p-4 border border-white/20">
        <p className="text-xs uppercase tracking-wider opacity-70 mb-1">Dîner</p>
        <p className="font-semibold text-lg">Soupe de Légumes</p>
      </div>
      <Link to="/planning" className="block mt-6 text-center bg-white text-blue-600 py-2 rounded-lg font-bold hover:bg-blue-50 transition">
        Gérer ma semaine
      </Link>
    </div>
  );
};

export default PlanningWidget;