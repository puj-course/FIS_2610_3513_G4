import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
const API_URL = `${BASE_URL}/api/auth`;

export interface RegisterRequest {
  email: string;
  password?: string; // El backend usa password, el frontend original de CRA usaba contrasena
  name: string;
  role: "CLIENT" | "BRAND" | "ADMIN";
  pictureUrl?: string;
  linkOfficial?: string;
}

export interface LoginRequest {
  email: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  role: string;
}

// En tu archivo AuthTypes.ts
export interface AuthUser {
  email: string;
  role: string;
  token: string; // <--- Asegúrate de que esto esté aquí
}

export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
  const response = await axios.post(`${API_URL}/register`, data);
  return response.data;
};

export const login = async (data: LoginRequest): Promise<AuthResponse> => {
  const response = await axios.post(`${API_URL}/login`, data);
  return response.data;
};
