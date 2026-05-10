import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
const API_URL = `${BASE_URL}/api/brands/public`;

export interface BrandPublic {
  id: number;
  name: string;
  email: string;
  pictureUrl: string;
  linkOfficial: string;
  followers: number;
  rating: number;
  isVerified: boolean;
}

export const getAllPublicBrands = async (): Promise<BrandPublic[]> => {
  const response = await axios.get(API_URL);
  return response.data;
};

export const getPublicBrandById = async (id: number): Promise<BrandPublic> => {
  const response = await axios.get(`${API_URL}/${id}`);
  return response.data;
};
