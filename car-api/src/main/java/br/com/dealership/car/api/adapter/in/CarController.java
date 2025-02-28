package br.com.dealership.car.api.adapter.in;


import br.com.dealership.car.api.adapter.in.dto.request.CarDtoRequest;
import br.com.dealership.car.api.adapter.in.dto.request.CarDtoUpdateRequest;
import br.com.dealership.car.api.adapter.in.dto.response.CarDtoResponse;
import br.com.dealership.car.api.adapter.in.dto.response.Response;
import br.com.dealership.car.api.adapter.mapper.CarMapper;
import br.com.dealership.car.api.adapter.mapper.SearchFilterMapper;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.CreateCarUseCase;
import br.com.dealership.car.api.core.usecase.DeleteCarUseCase;
import br.com.dealership.car.api.core.usecase.SearchCarUseCase;
import br.com.dealership.car.api.core.usecase.UpdateCarUseCase;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import java.math.BigDecimal;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class CarController {

    private final CreateCarUseCase createCarUseCase;
    private final DeleteCarUseCase deleteCarUseCase;
    private final SearchCarUseCase searchCarUseCase;
    private final UpdateCarUseCase updateCarUseCase;
    private final CarMapper carMapper;
    private final SearchFilterMapper searchFilterMapper;

    @Operation(summary = "Save a car in the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "The car was created with success"),
        @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
        @ApiResponse(responseCode = "408", description = "The request timed out"),
        @ApiResponse(responseCode = "409", description = "There was a conflict when creating the car"),
        @ApiResponse(responseCode = "500", description = "There was internal server errors"),
        @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
        @ApiResponse(responseCode = "503", description = "The service is unavailable"),
        @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/cars", produces = "application/json")
    @Timed(value="tempo_processamento_carro",histogram = true, percentiles = {0.5,0.9,0.95,0.99})
    public ResponseEntity<Response<CarDtoResponse>> createCar(@RequestBody @Valid final CarDtoRequest request) throws CarAlreadyExistsException {

        final var carModel = carMapper.toCarModel(request);

        final var createdCar = createCarUseCase.execute(carModel);

        final var carDtoResponse = carMapper.toCarDtoResponse(createdCar);

        final var response = Response.createResponse(carDtoResponse);

        return ResponseEntity.created(URI.create("/v1/dealership/cars/" + carDtoResponse.vin())).body(response);
    }

    @Operation(summary="Return a page of cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Return a list of cars"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server errors"),
            @ApiResponse(responseCode = "503", description = "The service is unavailable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path = "/cars", produces = "application/json")
    public ResponseEntity<Response<PageImpl<CarDtoResponse>>> searchAllCars(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) final Pageable pageable,
                                                                            @RequestParam(required = false, defaultValue = "0") final BigDecimal initialValue,
                                                                            @RequestParam(required = false) final BigDecimal finalValue,
                                                                            @RequestParam(required = false) final String modelYear,
                                                                            @RequestParam(required = false) final String model,
                                                                            @RequestParam(required = false) final String manufacturer) {

        final var searchFilters = searchFilterMapper.toSearchFilter(initialValue,finalValue,model,modelYear,manufacturer);

        final var cars =  searchCarUseCase.execute(pageable,searchFilters);

        final var carsDtoResponseList = cars.stream()
                .map(carMapper::toCarDtoResponse)
                .toList();

        final var response = Response.createResponse(new PageImpl<>(
                carsDtoResponseList,cars.getPageable(),cars.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="Return one car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The car was returned with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404",description = "There wasn't a car with the VIN that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path="/cars/{vin}",produces = "application/json")
    public ResponseEntity<Response<CarDtoResponse>> searchCarByVin(@PathVariable(value="vin") final String vin) throws CarNotFoundException {
        final var carModel = searchCarUseCase.execute(vin);

        final var response = Response.createResponse(carMapper.toCarDtoResponse(carModel));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a car color or/and a Car Value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The car was update with success."),
            @ApiResponse(responseCode = "404",description = "There wasn't a car with the VIN that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PutMapping(path="/cars/{vin}",produces = "application/json")
    public ResponseEntity<Response<CarDtoResponse>> updateCar(@PathVariable(value = "vin") final String vin, @RequestBody final CarDtoUpdateRequest carUpdate) throws CarNotFoundException {

        final var updatedCar = updateCarUseCase.execute(vin, carUpdate.color(), carUpdate.value());

        final var response = Response.createResponse(carMapper.toCarDtoResponse(updatedCar));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a car passing the VIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The car was deleted with success"),
            @ApiResponse(responseCode = "404",description = "There wasn't a car with the VIN that was informed in the database."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @DeleteMapping(path = "/cars/{vin}", produces = "application/json")
    public ResponseEntity<Response<String>> deleteCar(@PathVariable(value = "vin") final String vin) throws CarNotFoundException {
        deleteCarUseCase.execute(vin);

        final var response = Response.createResponse("Car with VIN: " + vin + " was deleted successfully");

        return ResponseEntity.ok(response);
    }

}
