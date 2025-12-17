import React from 'react';
import AuthForm from '../component/auth/AuthForm';
import { Link } from 'react-router-dom';

const LoginPage: React.FC = () => (
  <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 px-4">
    <h1 className="text-4xl font-extrabold text-blue-600 mb-8">MiamPlanning</h1>
    <AuthForm mode="login" />
    <p className="mt-6 text-sm text-gray-600">
      Pas encore de compte ? <Link to="/register" className="text-blue-600 font-bold hover:underline">S'inscrire</Link>
    </p>
  </div>
);

export default LoginPage;