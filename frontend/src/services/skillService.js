import api from "./api";

export const getSkills = () => {
  return api.get("/profile/me/skills");
};

export const addSkill = (data) => {
  return api.post("/profile/me/skills", data);
};

export const deleteSkill = (id) => {
  return api.delete(`/profile/me/skills/${id}`);
};