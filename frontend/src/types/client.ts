export interface Client {
  cpf: string;
  name: string;
  email: string;
  phone: string;
  address: Address;
  registrationDate: string;
}

export interface Address {
  street: string;
  number: string;
  complement?: string;
  neighborhood: string;
  city: string;
  state: string;
  postCode: string;
}

export interface CreateClientRequest {
  cpf: string;
  name: string;
  email: string;
  phone: string;
  address: Address;
}

export interface UpdateClientRequest {
  postCode?: string;
  streetNumber?: string;
}

export interface ClientSearchFilters {
  city?: string;
  state?: string;
}

export interface ClientSearchResponse {
  content: Client[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
} 