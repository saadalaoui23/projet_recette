import { useState } from 'react';
import './App.css';
import Login from './pages/LoginPage';
import Register from './pages/Register';

function App() {
  const [showLogin, setShowLogin] = useState(true);

  return (
    <div className="App">
      <nav style={{ marginBottom: '20px' }}>
        <button onClick={() => setShowLogin(true)} style={{ marginRight: '10px' }}>Login</button>
        <button onClick={() => setShowLogin(false)}>Register</button>
      </nav>

      <h1>{showLogin ? 'Connexion' : 'Inscription'}</h1>
      {showLogin ? <Login /> : <Register />}
    </div>
  );
}

export default App;