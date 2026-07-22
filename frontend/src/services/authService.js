import api from './api';

export const register = async (payload) => {
  const response = await api.post('/auth/register', payload);
  return response.data;
};

export const login = async (payload) => {
  const response = await api.post('/auth/login', payload);

  // Simpan token
  localStorage.setItem('token', response.data.token);
  localStorage.setItem('user', JSON.stringify(response.data));

  return response.data;
};

export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  const user = localStorage.getItem('user');
  return user ? JSON.parse(user) : null;
};