import api from "./api";

export const getMyProfile = async () => {
  const response = await api.get("/profile/me");
  return response.data;
};

export const updateProfile = async (profile) => {
  const response = await api.put("/profile/me", profile);
  return response.data;
};

export const updatePrivacy = async (privacy) => {
  const response = await api.put("/profile/me/privacy", {
    contactPrivacy: privacy,
  });

  return response.data;
};