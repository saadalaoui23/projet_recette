import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import HomePage from './pages/HomePage';
import RecettesPage from './pages/RecettesPage';
import PlanningPage from './pages/PlanningPage';
import NotFoundPage from './pages/NotFoundPage';

const PrivateRoute = ({ children }: { children: JSX.Element }) => {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" replace />;
};

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/home" element={<PrivateRoute><HomePage /></PrivateRoute>} />
        <Route path="/recettes" element={<PrivateRoute><RecettesPage /></PrivateRoute>} />
        <Route path="/planning" element={<PrivateRoute><PlanningPage /></PrivateRoute>} />
        <Route path="/" element={<Navigate to="/home" replace />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </Router>
  );
}
export default App;