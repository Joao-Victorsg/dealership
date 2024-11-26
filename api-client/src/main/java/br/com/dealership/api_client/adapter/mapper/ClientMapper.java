package br.com.dealership.api_client.adapter.mapper;

import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.response.ClientDtoResponse;
import br.com.dealership.api_client.adapter.out.database.entity.ClientEntity;
import br.com.dealership.api_client.core.domain.ClientModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final AddressMapper addressMapper;

    public ClientModel toModel(ClientDtoRequest clientRequest){

        final var addressModel = addressMapper.toModel(clientRequest.address());

        return ClientModel.builder()
                .cpf(clientRequest.cpf())
                .name(clientRequest.name())
                .clientAddress(addressModel)
                .build();
    }

    public ClientModel toModel(ClientEntity clientEntity){

        final var addressModel = addressMapper.toModel(clientEntity.getAddress());

        return ClientModel.builder()
                .cpf(clientEntity.getCpf())
                .name(clientEntity.getName())
                .clientAddress(addressModel)
                .registrationDate(clientEntity.getRegistrationDate())
                .build();
    }

    public ClientEntity toEntity(ClientModel clientModel){
        final var addressEntity = addressMapper.toEntity(clientModel.clientAddress());

        return ClientEntity.builder()
                .cpf(clientModel.cpf())
                .name(clientModel.name())
                .address(addressEntity)
                .registrationDate(LocalDateTime.now())
                .build();
    }

    public ClientDtoResponse toDtoResponse(ClientModel clientModel){

        final var addressDtoResponse = addressMapper.toDtoResponse(clientModel.clientAddress());

        return ClientDtoResponse.builder()
                .name(clientModel.name())
                .cpf(clientModel.cpf())
                .registrationDate(clientModel.registrationDate())
                .address(addressDtoResponse)
                .build();
    }

}
