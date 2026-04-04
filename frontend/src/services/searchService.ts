import axios from "axios";

const API_URL = "http://localhost:8080/api/products/search";

export interface Product {
    id: number;
    name: string;
    description: string;
    brandId: number;
    brandName: string;
    brandPictureUrl: string;
    brandIsVerified: boolean;
    productTypeName: string;
    category: string;
    price: number;
    generalFit: string;
    gender: string;
    color: string;
    available: boolean;
    rating: number;
    linkProduct: string;
    imageUrls: string[];
    tags: string[];
    createdAt: string;
}

export interface SearchResponse {
    products: Product[];
    currentPage: number;
    totalPages: number;
    totalResults: number;
    pageSize: number;
}

export interface SearchParams {
    keyword?: string;
    productTypeName?: string;
    category?: string;
    generalFit?: string;
    gender?: string;
    color?: string;
    available?: boolean;
    minPrice?: number;
    maxPrice?: number;
    tags?: string[];
    page?: number;
    size?: number;
}

export const searchProducts = async (params: SearchParams): Promise<SearchResponse> => {
    // Convert tags array to comma-separated string if axios doesn't handle it as expected by Spring 
    // or keep as array if Spring expects multiple params with same name.
    // Spring Boot usually handles List<String> tags as ?tags=v1&tags=v2
    const response = await axios.get(API_URL, { params });
    return response.data;
};

export const reindexProducts = async (token: string): Promise<string> => {
    const response = await axios.post(`${API_URL}/reindex`, {}, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};
