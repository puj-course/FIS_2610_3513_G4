import axios from "axios";

const API_URL = "http://localhost:8080/api";

export interface ProductType {
    id: number;
    name: string;
    category: string;
}

export interface Tag {
    id: number;
    name: string;
    type: string;
}

export const getProductTypes = async (token: string): Promise<ProductType[]> => {
    const response = await axios.get(`${API_URL}/product-types`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

export const getTags = async (token: string): Promise<Tag[]> => {
    const response = await axios.get(`${API_URL}/tags`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};
