import api from './api';

export const getMyProfile = async () => {
  const response = await api.get('/profile/me');
  return response.data;
};

export const updateProfile = async (payload) => {
  const response = await api.put('/profile/me', payload);
  return response.data;
};

export const getMySkills = async () => {
  const response = await api.get('/profile/me/skills');
  return response.data;
};

export const addSkill = async (skillId, level) => {
  const response = await api.post('/profile/me/skills', {
    skillId,
    level,
  });

  return response.data;
};

export const removeSkill = async (skillId) => {
  await api.delete(`/profile/me/skills/${skillId}`);
};