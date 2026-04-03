import axios from "axios";

const API_URL = "http://localhost:8080/api/clients";

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
