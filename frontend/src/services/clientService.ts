import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
const API_URL = `${BASE_URL}/api/clients`;

export interface ClientProfile {
  name: string;
  email: string;
}

export interface PasswordChangeRequest {
  currentPassword: string;
  newPassword: string;
}

export const getProfile = async (token: string): Promise<ClientProfile> => {
  const response = await axios.get(`${API_URL}/profile`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

export const updateProfile = async (
  token: string,
  data: { name: string }
): Promise<ClientProfile> => {
  const response = await axios.put(`${API_URL}/profile`, data, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

export const changePassword = async (
  token: string,
  data: PasswordChangeRequest
): Promise<void> => {
  await axios.put(`${API_URL}/password`, data, {
    headers: { Authorization: `Bearer ${token}` },
  });
};

export const followBrand = async (brandId: number, token: string): Promise<void> => {
  await axios.post(`${BASE_URL}/api/clients/profile/following/${brandId}`, {}, {
    headers: { Authorization: `Bearer ${token}` },
  });
};

export const unfollowBrand = async (brandId: number, token: string): Promise<void> => {
  await axios.delete(`${BASE_URL}/api/clients/profile/following/${brandId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
};

export const getFollowedBrands = async (token: string): Promise<any[]> => {
  const response = await axios.get(`${BASE_URL}/api/clients/profile/following`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

// --- FAVORITOS (Wishlists) ---

export const addToFavorites = async (productId: number, token: string): Promise<void> => {
  await axios.post(`${BASE_URL}/api/clients/profile/wishlists/default/products/${productId}`, {}, {
    headers: { Authorization: `Bearer ${token}` },
  });
};

export const removeFromFavorites = async (productId: number, token: string): Promise<void> => {
  await axios.delete(`${BASE_URL}/api/clients/profile/wishlists/default/products/${productId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
};

export const getDefaultWishlist = async (token: string): Promise<any> => {
  const response = await axios.get(`${BASE_URL}/api/clients/profile/wishlists/default`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};
