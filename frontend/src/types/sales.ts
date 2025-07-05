export interface SalesRequest {
  cpf: string;
  vin: string;
}

export interface Sales {
  id: string;
  cpf: string;
  vin: string;
  registrationDate: string;
}

export interface SalesSearchFilters {
  initialDate?: string;
  finalDate?: string;
  cpf?: string;
}

export interface SalesSearchResponse {
  content: Sales[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
} 