import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

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

        <Route
          path="/"
          element={<Navigate to="/login" />}
        />

        <Route
          path="/login"
          element={<LoginView />}
        />

        <Route
          path="/register"
          element={<RegisterView />}
        />

        <Route
          path="/dashboard"
          element={<DashboardView />}
        />

        <Route
          path="/profile"
          element={<ProfileView />}
        />

        <Route
          path="/projects/create"
          element={<CreateProjectView />}
        />

        <Route
          path="/projects/:id"
          element={<ProjectDetailView />}
        />

        <Route
          path="/my-projects"
          element={<MyProjectView />}
        />

      </Routes>
    </Router>
  );
}

export default App;