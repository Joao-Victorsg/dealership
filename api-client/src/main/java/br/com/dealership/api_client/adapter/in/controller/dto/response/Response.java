package br.com.dealership.api_client.adapter.in.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class Response<T> {

    private final T data;

    public static <T> Response<T> createResponse(final T data){
        return new Response<>(data);
    }

    public static Response<ResponseError> createResponseWithError(final ResponseError error){
        return new Response<>(error);
    }
}