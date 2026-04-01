import axios from "axios";

const API_URL = "http://localhost:8080/api/products/search";

export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    pictureUrl: string;
    brandName: string;
    category: string;
    gender: string;
    available: boolean;
}

export interface SearchResponse {
    content: Product[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}

export interface SearchParams {
    keyword?: string;
    category?: string;
    gender?: string;
    minPrice?: number;
    maxPrice?: number;
    page?: number;
    size?: number;
}

export const searchProducts = async (params: SearchParams): Promise<SearchResponse> => {
    const response = await axios.get(API_URL, { params });
    return response.data;
};

export const reindexProducts = async (token: string): Promise<string> => {
    const response = await axios.post(`${API_URL}/reindex`, {}, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

