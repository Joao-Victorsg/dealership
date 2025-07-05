export interface Car {
  id: string;
  model: string;
  modelYear: string;
  manufacturer: string;
  color: string;
  vin: string;
  value: number;
  registrationDate: string;
}

export interface CreateCarRequest {
  model: string;
  modelYear: string;
  manufacturer: string;
  color: string;
  vin: string;
  value: number;
}

export interface UpdateCarRequest {
  color?: string;
  value?: number;
  modelYear?: string;
}

export interface CarSearchFilters {
  initialValue?: number;
  finalValue?: number;
  modelYear?: string;
  model?: string;
  manufacturer?: string;
  color?: string;
}

export interface CarSearchResponse {
  content: Car[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  timestamp: string;
} 