package br.com.dealership.car.api.adapter.in.dto.response;

public record Response<T> (
        T data
){
    public static <T> Response<T> createResponse(final T data){
        return new Response<>(data);
    }

    public static Response<ResponseError> createResponseWithError(final ResponseError error){
        return new Response<>(error);
    }
}

