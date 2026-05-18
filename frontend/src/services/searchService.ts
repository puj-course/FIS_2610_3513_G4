import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
const API_URL = `${BASE_URL}/api/products/search`;

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

// ProductSearchRequest.java usa "productType" (no "productTypeName")
export interface SearchParams {
    query?: string;
    productType?: string;
    category?: string;
    generalFit?: string;
    gender?: string;
    color?: string;
    minPrice?: number;
    maxPrice?: number;
    tags?: string[];
    page?: number;
    size?: number;
}

export const searchProducts = async (params: SearchParams): Promise<SearchResponse> => {
    const response = await axios.post(API_URL, params);
    return {
        searchedProducts: response.data.products || response.data.searchedProducts || [],
        currentPage: response.data.currentPage || 0,
        totalPages: response.data.totalPages || 0,
        totalResults: response.data.totalResults || 0,
        pageSize: response.data.pageSize || 12,
    };
};
