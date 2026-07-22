import api from './api';

// Ambil semua proyek
export const getProjects = async (keyword = '') => {
  const response = await api.get('/projects', {
    params: keyword ? { q: keyword } : {},
  });

  return response.data;
};

// Detail proyek
export const getProjectById = async (id) => {
  const response = await api.get(`/projects/${id}`);
  return response.data;
};

// Proyek saya
export const getMyProjects = async () => {
  const response = await api.get('/projects/my');
  return response.data;
};

// Buat proyek
export const createProject = async (payload) => {
  const response = await api.post('/projects', payload);
  return response.data;
};

// Lamar proyek
export const applyProject = async (projectId, payload) => {
  const response = await api.post(
    `/projects/${projectId}/apply`,
    payload
  );

  return response.data;
};

// Lihat pelamar
export const getProjectApplications = async (projectId) => {
  const response = await api.get(
    `/projects/${projectId}/applications`
  );

  return response.data;
};

// Terima pelamar
export const acceptApplication = async (projectId, appId) => {
  const response = await api.put(
    `/projects/${projectId}/applications/${appId}/accept`
  );

  return response.data;
};

// Tolak pelamar
export const rejectApplication = async (projectId, appId) => {
  const response = await api.put(
    `/projects/${projectId}/applications/${appId}/reject`
  );

  return response.data;
};