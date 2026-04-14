import axios from "axios";

const API_URL = "http://localhost:8080/api/brands";

export interface BrandProfile {
    id?: number;
    name: string;
    email: string;
    pictureUrl: string;
    linkOfficial: string;
    followers: number;
    rating: number;
    isVerified: boolean;
}

export interface UpdateBrandData {
    name: string;
    pictureUrl: string;
    linkOfficial: string;
}

export interface PasswordData {
    currentPassword: string;
    newPassword: string;
}

export interface ProductManagement {
    id: number;
    name: string;
    description: string;
    price: number;
    available: boolean;
    imageUrls: string[];
    linkProduct?: string;
    productType: {
        id: number;
        name: string;
        category: string;
    };
    generalFit: string;
    gender: string;
    color: string;
    tags: Array<{
        id: number;
        name: string;
        type: string;
    }>;
    rating: number;
    createdAt: string;
}

export interface ProductRequest {
    name: string;
    description: string;
    price: number;
    available: boolean;
    imageUrls: string[];
    linkProduct?: string;
    productTypeId: number;
    generalFit: string;
    gender: string;
    color: string;
    tagIds: number[];
}

export const getBrandProfile = async (token: string): Promise<BrandProfile> => {
    const response = await axios.get(`${API_URL}/profile`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

export const updateBrandProfile = async (token: string, data: UpdateBrandData): Promise<BrandProfile> => {
    const response = await axios.put(`${API_URL}/profile`, data, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

export const changeBrandPassword = async (token: string, data: PasswordData): Promise<void> => {
    await axios.put(`${API_URL}/password`, data, {
        headers: { Authorization: `Bearer ${token}` }
    });
};

export const getMyProducts = async (token: string): Promise<ProductManagement[]> => {
    const response = await axios.get(`${API_URL}/my-products`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

export const createProduct = async (token: string, data: ProductRequest): Promise<ProductManagement> => {
    const response = await axios.post(`${API_URL}/my-products`, data, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

export const updateProduct = async (token: string, id: number, data: ProductRequest): Promise<ProductManagement> => {
    const response = await axios.put(`${API_URL}/my-products/${id}`, data, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};

export const deleteProduct = async (token: string, id: number): Promise<void> => {
    await axios.delete(`${API_URL}/my-products/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
    });
};
