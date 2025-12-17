import React from 'react';
import { Link } from 'react-router-dom';

const Header: React.FC = () => {
  return (
    <nav className="bg-white shadow-sm border-b px-6 py-4 flex justify-between items-center">
      <h1 className="text-2xl font-bold text-blue-600">MiamPlanning</h1>
      <div className="space-x-6 text-gray-600 font-medium">
        <Link to="/home" className="hover:text-blue-600 transition">Accueil</Link>
        <Link to="/recettes" className="hover:text-blue-600 transition">Recettes</Link>
        <Link to="/planning" className="hover:text-blue-600 transition">Planning</Link>
        <button className="bg-red-50 text-red-600 px-4 py-2 rounded-lg text-sm font-semibold hover:bg-red-100 transition">
          Déconnexion
        </button>
      </div>
    </nav>
  );
};

export default Header;