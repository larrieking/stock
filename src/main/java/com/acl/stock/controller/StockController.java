package com.acl.stock.controller;

import com.acl.stock.domain.request.StockFilterRequest;
import com.acl.stock.domain.response.AppResponse;
import com.acl.stock.domain.response.PagedResponse;
import com.acl.stock.domain.response.StockResponse;
import com.acl.stock.service.StockService;
import com.acl.stock.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@RestController
@RequestMapping("api")
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Api(value = "api", tags = "Stock Management API")
@ApiOperation(value = "Stock Controller API")
@RequiredArgsConstructor
public class StockController {

private final StockService service;

    @GetMapping(path = "/stocks")
    @ApiOperation(value = "Fetch all stocks", notes = "This endpoint fetches all stock")
    public ResponseEntity<AppResponse<PagedResponse<StockResponse>>> queryClients(@Valid StockFilterRequest request) {
        PagedResponse<StockResponse> clientResponse = service.findAllStock(request);
        AppResponse<PagedResponse<StockResponse>> response = AppResponse.<PagedResponse<StockResponse>>builder().message(AppConstants.ApiResponseMessage.SUCCESSFUL)
                .status(HttpStatus.OK.value()).data(clientResponse).error("").build();
        return ResponseEntity.ok().body(response);
    }
}
