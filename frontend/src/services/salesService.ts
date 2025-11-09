import { ApiResponse } from '../types/car';
import { Sales, SalesRequest, SalesSearchFilters, SalesSearchResponse } from '../types/sales';
import { salesApiClient } from './apiClient';

export const salesService = {
  // Create a new sale
  async createSale(sale: SalesRequest): Promise<ApiResponse<Sales>> {
    const response = await salesApiClient.post('/sales', sale);
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

    const response = await salesApiClient.get(`/sales?${params}`);
    return response.data;
  },

  // Get sale by ID
  async getSaleById(id: string): Promise<ApiResponse<Sales>> {
    const response = await salesApiClient.get(`/sales/${id}`);
    return response.data;
  },

  // Cancel sale
  async cancelSale(id: string): Promise<ApiResponse<string>> {
    const response = await salesApiClient.delete(`/sales/${id}`);
    return response.data;
  },
}; 