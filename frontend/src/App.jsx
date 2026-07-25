import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginView from './auth/LoginView';
import RegisterView from './auth/RegisterView';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />
        
        {/* Placeholder untuk Dashboard agar tidak error setelah login */}
        <Route path="/dashboard" element={
            <div className="flex items-center justify-center min-h-screen">
                <h1 className="text-3xl font-bold text-green-600">Token JWT Berhasil Disimpan! Dashboard segera hadir.</h1>
            </div>
        } />
      </Routes>
    </Router>
  );
}

export default App;