import { ApiResponse, Car, CarSearchFilters, CarSearchResponse, CreateCarRequest, UpdateCarRequest } from '../types/car';
import { carApiClient, publicCarApiClient } from './apiClient';

export const carService = {
  // Get all cars with pagination and filters
  async getCars(
    page: number = 0,
    size: number = 10,
    filters?: CarSearchFilters
  ): Promise<ApiResponse<CarSearchResponse>> {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    });

    // Only add filter parameters if they have meaningful values
    if (filters?.initialValue && filters.initialValue > 0) {
      params.append('initialValue', filters.initialValue.toString());
    }
    if (filters?.finalValue && filters.finalValue < 100000) {
      params.append('finalValue', filters.finalValue.toString());
    }
    if (filters?.modelYear && filters.modelYear.trim()) {
      params.append('modelYear', filters.modelYear);
    }
    if (filters?.model && filters.model.trim()) {
      params.append('model', filters.model);
    }
    if (filters?.manufacturer && filters.manufacturer.trim()) {
      params.append('manufacturer', filters.manufacturer);
    }
    if (filters?.color && filters.color.trim()) {
      params.append('color', filters.color);
    }

    const response = await carApiClient.get(`/cars?${params}`);
    return response.data;
  },

  // Get car by VIN
  async getCarByVin(vin: string): Promise<ApiResponse<Car>> {
    const response = await carApiClient.get(`/cars/${vin}`);
    return response.data;
  },

  // Create a new car
  async createCar(car: CreateCarRequest): Promise<ApiResponse<Car>> {
    const response = await carApiClient.post('/cars', car);
    return response.data;
  },

  // Update a car
  async updateCar(vin: string, car: UpdateCarRequest): Promise<ApiResponse<Car>> {
    const response = await carApiClient.put(`/cars/${vin}`, car);
    return response.data;
  },

  // Delete a car
  async deleteCar(vin: string): Promise<ApiResponse<string>> {
    const response = await carApiClient.delete(`/cars/${vin}`);
    return response.data;
  },

  // Get all distinct manufacturers
  async getManufacturers(): Promise<ApiResponse<string[]>> {
    const response = await carApiClient.get('/cars/manufacturers');
    return response.data;
  },

  // Get all distinct models
  async getModels(): Promise<ApiResponse<string[]>> {
    const response = await carApiClient.get('/cars/models');
    return response.data;
  },

  // Get models by manufacturer
  async getModelsByManufacturer(manufacturer: string): Promise<ApiResponse<string[]>> {
    const response = await carApiClient.get(`/cars/models/${encodeURIComponent(manufacturer)}`);
    return response.data;
  },
};

// Public car service - no authentication required
export const publicCarService = {
  // Get all cars with pagination and filters
  async getCars(
    page: number = 0,
    size: number = 10,
    filters?: CarSearchFilters
  ): Promise<ApiResponse<CarSearchResponse>> {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    });

    // Only add filter parameters if they have meaningful values
    if (filters?.initialValue && filters.initialValue > 0) {
      params.append('initialValue', filters.initialValue.toString());
    }
    if (filters?.finalValue && filters.finalValue < 100000) {
      params.append('finalValue', filters.finalValue.toString());
    }
    if (filters?.modelYear && filters.modelYear.trim()) {
      params.append('modelYear', filters.modelYear);
    }
    if (filters?.model && filters.model.trim()) {
      params.append('model', filters.model);
    }
    if (filters?.manufacturer && filters.manufacturer.trim()) {
      params.append('manufacturer', filters.manufacturer);
    }
    if (filters?.color && filters.color.trim()) {
      params.append('color', filters.color);
    }

    const response = await publicCarApiClient.get(`/cars?${params}`);
    return response.data;
  },

  // Get all distinct manufacturers
  async getManufacturers(): Promise<ApiResponse<string[]>> {
    const response = await publicCarApiClient.get('/cars/manufacturers');
    return response.data;
  },

  // Get all distinct models
  async getModels(): Promise<ApiResponse<string[]>> {
    const response = await publicCarApiClient.get('/cars/models');
    return response.data;
  },

  // Get models by manufacturer
  async getModelsByManufacturer(manufacturer: string): Promise<ApiResponse<string[]>> {
    const response = await publicCarApiClient.get(`/cars/models/${encodeURIComponent(manufacturer)}`);
    return response.data;
  },
};