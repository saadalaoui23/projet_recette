import React from 'react';
import AuthForm from '../component/auth/AuthForm';
import { Link } from 'react-router-dom';

const RegisterPage: React.FC = () => (
  <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 px-4">
    <h1 className="text-4xl font-extrabold text-green-600 mb-8">MiamPlanning</h1>
    <AuthForm mode="register" />
    <p className="mt-6 text-sm text-gray-600">
      Déjà inscrit ? <Link to="/login" className="text-green-600 font-bold hover:underline">Se connecter</Link>
    </p>
  </div>
);

export default RegisterPage;