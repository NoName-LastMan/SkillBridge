import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginView from './auth/LoginView';
import RegisterView from './auth/RegisterView';
import DashboardView from './projects/DashboardView';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />
        <Route path="/dashboard" element={<DashboardView />} />
      </Routes>
    </Router>
  );
}

export default App;