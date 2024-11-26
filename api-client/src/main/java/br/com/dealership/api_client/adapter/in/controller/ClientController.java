package br.com.dealership.api_client.adapter.in.controller;

import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoUpdateRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.response.ClientDtoResponse;
import br.com.dealership.api_client.adapter.in.controller.dto.response.Response;
import br.com.dealership.api_client.adapter.mapper.ClientMapper;
import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.CreateClientUseCase;
import br.com.dealership.api_client.core.usecase.DeleteClientUseCase;
import br.com.dealership.api_client.core.usecase.GetClientUseCase;
import br.com.dealership.api_client.core.usecase.UpdateClientUseCase;
import br.com.dealership.api_client.utils.Logger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final ClientMapper clientMapper;

    @Operation(summary = "Return a page of clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of clients"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path = "/clients", produces = "application/json")
    public ResponseEntity<Response<PageImpl<ClientDtoResponse>>> getAllClient(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                                                                              @RequestParam(required = false) final String city,
                                                                              @RequestParam(required = false) final String state) {
        final var modelList = getClientUseCase.execute(pageable,city,state);

        final var dtoResponse = modelList.stream()
                .map(clientMapper::toDtoResponse)
                .toList();

        final var response = Response.createResponse(new PageImpl<>(dtoResponse,modelList.getPageable(),modelList.getTotalElements()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Return one client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was returned with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "There wasn't a client with the CPF that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path = "/clients/{cpf}", produces = "application/json")
    public ResponseEntity<Response<ClientDtoResponse>> getClient(@PathVariable(value = "cpf") final String cpf) throws ClientNotFoundException {
        final var model = getClientUseCase.execute(cpf);

        final var dtoResponse = clientMapper.toDtoResponse(model);

        final var response = Response.createResponse(dtoResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Save a client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The client was created with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the client"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unavailable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/clients", produces = "application/json")
    public ResponseEntity<Response<ClientDtoResponse>> createClient(@RequestBody @Valid final ClientDtoRequest request) throws ClientAlreadyExistsException {
        Logger.info("Starting process to create the client: ",request);

        final var clientModel = clientMapper.toModel(request);

        final var createdClientModel = createClientUseCase.execute(clientModel);

        final var clientDtoResponse = clientMapper.toDtoResponse(createdClientModel);

        final var response = Response.createResponse(clientDtoResponse);

        Logger.info("Client created with success",clientDtoResponse);

        return ResponseEntity.created(URI.create("/v1/dealership/client/" + clientDtoResponse.cpf()))
                .body(response);
    }

    @Operation(summary = "Update a client name or/and address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was update with success."),
            @ApiResponse(responseCode = "404", description = "There wasn't a client with the VIN that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unavailable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PutMapping(path = "/clients/{cpf}", produces = "application/json")
    public ResponseEntity<Response<ClientDtoResponse>> updateClient(@PathVariable(value = "cpf") final String cpf, @RequestBody final ClientDtoUpdateRequest request) throws ClientNotFoundException {
        final var addressUpdateInfo = AddressModel.builder()
                .postCode(request.postCode())
                .streetNumber(request.streetNumber())
                .build();

        final var updatedClient = updateClientUseCase.execute(cpf,addressUpdateInfo);

        final var responseDto = clientMapper.toDtoResponse(updatedClient);

        final var response = Response.createResponse(responseDto);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a client passing the CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was deleted with success"),
            @ApiResponse(responseCode = "404", description = "There wasn't a client with the CPF that was informed in the database."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @DeleteMapping(path = "/clients/{cpf}", produces = "application/json")
    public ResponseEntity<Response<String>> deleteClient(@PathVariable(value = "cpf") final String cpf) throws ClientNotFoundException {
        deleteClientUseCase.execute(cpf);

        final var response = Response.createResponse("Client with CPF: " + cpf + " was successfully deleted");

        return ResponseEntity.ok(response);
    }
}