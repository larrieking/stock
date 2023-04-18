package com.acl.stock.domain.response;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;


@Builder
@Data
@AllArgsConstructor
public class AppResponse<T> {

    @ApiParam(value = "HTTP status code")
    private int status;

    @ApiParam(value = "Description of http status code")
    private String message;

    @ApiParam(value = "Response data")
    private T data;

    @ApiParam(value = "Time taken to process request(for metrics and performance tracking)")
    @Builder.Default
    private Double execTime = 0D;

    @ApiParam(value = "Specific errors when request does not return a HTTP status of 200.")
    @Builder.Default
    private Object error = new ArrayList<>();
}