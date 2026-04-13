import axios from "axios";

const API_URL = "http://localhost:8080/api";

export interface TagDetail {
  id: number;
  name: string;
  type: string; // "FIT" | "STYLE" | "OCCASION"
}

export interface ProductTypeDetail {
  id: number;
  name: string;
  category: string;
}

/** Respuesta completa de GET /api/products/{id} — sin campos de marca */
export interface ProductDetail {
  id: number;
  brandId: number;
  productType: ProductTypeDetail;
  name: string;
  description: string;
  price: number;
  generalFit: string;
  gender: string;
  color: string;
  available: boolean;
  rating: number | null;
  linkProduct: string;
  createdAt: string;
  imageUrls: string[];
  tags: TagDetail[];
}

export const getProductById = async (id: number): Promise<ProductDetail> => {
  const response = await axios.get(`${API_URL}/products/${id}`);
  return response.data;
};
