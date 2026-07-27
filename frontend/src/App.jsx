import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import ProtectedRoute from "./components/ProtectedRoute";
import HomeView from './auth/HomeView';
import LoginView from "./auth/LoginView";
import RegisterView from "./auth/RegisterView";
import EditProjectView from "./projects/EditProjectView";

import ProfileView from "./profile/ProfileView";
import PublicProfileView from './profile/PublicProfileView'; // Pastikan path-nya benar

import DashboardView from "./projects/DashboardView";
import CreateProjectView from "./projects/CreateProjectView";
import ProjectDetailView from "./projects/ProjectDetailView";
import MyProjectView from "./projects/MyProjectView";

function App() {
  return (
    <Router>
      <Routes>
        {/* =====================================
            Rute Publik (Tidak perlu login) 
            ===================================== */}
        <Route path="/" element={<HomeView />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />

        {/* =====================================
            Rute Privat (Wajib login) 
            ===================================== */}
        <Route 
          path="/dashboard" 
          element={
            <ProtectedRoute>
              <DashboardView />
            </ProtectedRoute>
          } 
        />
        
        {/* Rute Edit Profil Sendiri */}
        <Route 
          path="/profile" 
          element={
            <ProtectedRoute>
              <ProfileView />
            </ProtectedRoute>
          } 
        />
        
        {/* Rute Lihat Profil Orang Lain */}
        <Route 
          path="/profile/:id" 
          element={
            <ProtectedRoute>
              <PublicProfileView />
            </ProtectedRoute>
          } 
        />

        <Route 
          path="/projects/create" 
          element={
            <ProtectedRoute>
              <CreateProjectView />
            </ProtectedRoute>
          } 
        />
        
        <Route 
          path="/projects/:id" 
          element={
            <ProtectedRoute>
              <ProjectDetailView />
            </ProtectedRoute>
          } 
        />
        
        <Route 
          path="/my-projects" 
          element={
            <ProtectedRoute>
              <MyProjectView />
            </ProtectedRoute>
          } 
        />
        <Route 
  path="/projects/edit/:id" 
  element={
    <ProtectedRoute>
      <EditProjectView />
    </ProtectedRoute>
  } 
/>
        
      </Routes>
    </Router>
  );
}

export default App;