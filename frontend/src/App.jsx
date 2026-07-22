import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Profile from './pages/Profile';
import Projects from './pages/Projects';
import ProjectDetail from './pages/ProjectDetail';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
  <Route path="/" element={<Login />} />
  <Route path="/login" element={<Login />} />
  <Route path="/profile" element={<Profile />} />
  <Route path="/projects" element={<Projects />} />
  <Route path="/projects/:id" element={<ProjectDetail />} />

  {/* route cadangan */}
  <Route path="*" element={<Login />} />
</Routes>
    </BrowserRouter>
  );
}