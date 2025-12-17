import React, { useState } from 'react';
import axios from 'axios';

const Register: React.FC = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  });
  const [status, setStatus] = useState({ message: '', error: false });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setStatus({ message: 'Inscription en cours...', error: false });

    try {
      // Test direct sur le port 8083
      const response = await axios.post('http://localhost:8083/api/users/register', formData);

      if (response.status === 200 || response.status === 201) {
        setStatus({ message: "Inscription réussie ! Vous pouvez maintenant vous connecter.", error: false });
      }
    } catch (err: any) {
      console.error(err);
      const errorMsg = err.response?.data?.message || err.message || "Erreur inconnue";
      setStatus({ message: `Erreur ${err.response?.status || ''}: ${errorMsg}`, error: true });
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '20px auto', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
      <h2>Inscription (Test CORS/Auth)</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input name="firstName" placeholder="Prénom" onChange={handleChange} required />
        <input name="lastName" placeholder="Nom" onChange={handleChange} required />
        <input name="email" type="email" placeholder="Email" onChange={handleChange} required />
        <input name="password" type="password" placeholder="Mot de passe" onChange={handleChange} required />
        <button type="submit" style={{ padding: '10px', cursor: 'pointer', backgroundColor: '#007bff', color: 'white', border: 'none' }}>
          S'inscrire
        </button>
      </form>
      {status.message && (
        <p style={{ color: status.error ? 'red' : 'green', marginTop: '15px' }}>{status.message}</p>
      )}
    </div>
  );
};

export default Register;