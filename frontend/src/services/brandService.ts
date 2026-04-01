import axios from "axios";

const API_URL = "http://localhost:8080/api/brands";

export interface BrandProfile {
    id: number;
    name: string;
    email: string;
    pictureUrl: string;
    linkOfficial: string;
    followersCount: number;
    rating: number;
    verified: boolean;
}

export const getBrandProfile = async (token: string): Promise<BrandProfile> => {
    const response = await axios.get(`${API_URL}/profile`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

export const getMyProducts = async (token: string): Promise<any[]> => {
    const response = await axios.get(`${API_URL}/my-products`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};
