import React from 'react';

interface RecetteProps {
  titre: string;
  description: string;
}

const RecetteCard: React.FC<RecetteProps> = ({ titre, description }) => {
  return (
    <div className="bg-white border border-gray-100 p-4 rounded-xl shadow-sm hover:shadow-md transition">
      <div className="h-32 bg-blue-50 rounded-lg mb-4 flex items-center justify-center text-3xl">
        🍲
      </div>
      <h4 className="font-bold text-gray-800 text-lg">{titre}</h4>
      <p className="text-gray-500 text-sm mt-1 line-clamp-2">{description}</p>
    </div>
  );
};

export default RecetteCard;