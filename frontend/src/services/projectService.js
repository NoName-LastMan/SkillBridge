import api from "./api";

// Ambil semua proyek
export const getProjects = async () => {
  const response = await api.get("/projects");
  return response.data;
};

// Ambil detail proyek berdasarkan ID
export const getProjectById = async (id) => {
  const response = await api.get(`/projects/${id}`);
  return response.data;
};

// Membuat proyek baru
export const createProject = async (projectData) => {
  const response = await api.post("/projects", projectData);
  return response.data;
};

// Apply / Join ke proyek
export const applyProject = async (id) => {
  const response = await api.post(`/projects/${id}/apply`);
  return response.data;
};

// Ambil proyek milik user
export const getMyProjects = async () => {
  const response = await api.get("/projects/me");
  return response.data;
};