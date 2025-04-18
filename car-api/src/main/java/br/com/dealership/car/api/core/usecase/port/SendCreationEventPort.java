package br.com.dealership.car.api.core.usecase.port;

public interface SendCreationEventPort {
    void sendCreationEvent(String vin);
}