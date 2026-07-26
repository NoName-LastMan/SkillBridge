import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import ProtectedRoute from "./components/ProtectedRoute";
import HomeView from './auth/HomeView';
import LoginView from "./auth/LoginView";
import RegisterView from "./auth/RegisterView";

import ProfileView from "./profile/ProfileView";

import DashboardView from "./projects/DashboardView";
import CreateProjectView from "./projects/CreateProjectView";
import ProjectDetailView from "./projects/ProjectDetailView";
import MyProjectView from "./projects/MyProjectView";

function App() {
  return (
    <Router>
      <Routes>
        {/* Rute Publik (Tidak perlu login) */}
        <Route path="/" element={<HomeView />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />

        {/* Rute Privat (Wajib login) */}
        <Route 
          path="/dashboard" 
          element={
            <ProtectedRoute>
              <DashboardView />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/profile" 
          element={
            <ProtectedRoute>
              <ProfileView />
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
      </Routes>
    </Router>
  );
}

export default App;