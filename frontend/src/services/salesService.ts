import axios from 'axios';
import { ApiResponse } from '../types/car';
import { Sales, SalesRequest, SalesSearchFilters, SalesSearchResponse } from '../types/sales';

// Sales API runs on port 8086 with context path /v1/dealership
const SALES_API_BASE_URL = process.env.REACT_APP_SALES_API_URL || 'http://localhost:8086/v1/dealership';

const salesApi = axios.create({
  baseURL: SALES_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const salesService = {
  // Create a new sale
  async createSale(sale: SalesRequest): Promise<ApiResponse<Sales>> {
    const response = await salesApi.post('/sales', sale);
    return response.data;
  },

  // Get all sales with pagination and filters
  async getSales(
    page: number = 0,
    size: number = 10,
    filters?: SalesSearchFilters
  ): Promise<ApiResponse<SalesSearchResponse>> {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      ...(filters?.initialDate && { initialDate: filters.initialDate }),
      ...(filters?.finalDate && { finalDate: filters.finalDate }),
      ...(filters?.cpf && { cpf: filters.cpf }),
    });

    const response = await salesApi.get(`/sales?${params}`);
    return response.data;
  },

  // Get sale by ID
  async getSaleById(id: string): Promise<ApiResponse<Sales>> {
    const response = await salesApi.get(`/sales/${id}`);
    return response.data;
  },

  // Cancel sale
  async cancelSale(id: string): Promise<ApiResponse<string>> {
    const response = await salesApi.delete(`/sales/${id}`);
    return response.data;
  },
}; 