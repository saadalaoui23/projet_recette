import React from 'react';
import { Link } from 'react-router-dom';

const NotFoundPage: React.FC = () => {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 px-4">
      <h1 className="text-6xl font-extrabold text-gray-800 mb-4">404</h1>
      <p className="text-gray-600 mb-6 text-center">
        Oups, la page que vous cherchez n'existe pas.
      </p>
      <Link
        to="/home"
        className="px-6 py-3 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 transition"
      >
        Retour à l'accueil
      </Link>
    </div>
  );
};

export default NotFoundPage;


