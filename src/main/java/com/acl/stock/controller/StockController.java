package com.acl.stock.controller;

import com.acl.stock.domain.request.CreateStockRequest;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AppResponse<PagedResponse<StockResponse>>> queryStocks(@Valid StockFilterRequest request) {
        PagedResponse<StockResponse> clientResponse = service.findAllStock(request);
        AppResponse<PagedResponse<StockResponse>> response = AppResponse.<PagedResponse<StockResponse>>builder().message(AppConstants.ApiResponseMessage.SUCCESSFUL)
                .status(HttpStatus.OK.value()).data(clientResponse).error("").build();
        return ResponseEntity.ok().body(response);
    }


    @PostMapping(path = "/stocks")
    @ApiOperation(value = "Create a stock", notes = "This endpoint Create a stock")
    public ResponseEntity<AppResponse<StockResponse>> createStock(@Valid CreateStockRequest request) {
        StockResponse clientResponse = service.createStock(request);
        AppResponse<StockResponse> response = AppResponse.<StockResponse>builder().message(AppConstants.ApiResponseMessage.SUCCESSFUL)
                .status(HttpStatus.OK.value()).data(clientResponse).error("").build();
        return ResponseEntity.ok().body(response);
    }


    @PutMapping(path = "/stocks/{id:\\d+}")
    @ApiOperation(value = "Update a stock", notes = "This endpoint update a stock")
    public ResponseEntity<AppResponse<StockResponse>> updateStock(@Valid @RequestBody CreateStockRequest request,@PathVariable Long id) {
        StockResponse clientResponse = service.updateStock(request, id);
        AppResponse<StockResponse> response = AppResponse.<StockResponse>builder().message(AppConstants.ApiResponseMessage.SUCCESSFUL)
                .status(HttpStatus.OK.value()).data(clientResponse).error("").build();
        return ResponseEntity.ok().body(response);
    }


    @GetMapping(path = "/stocks/{id:\\d+}")
    @ApiOperation(value = "Get a stock", notes = "This endpoint gets a stock")
    public ResponseEntity<AppResponse<StockResponse>> getStock(@PathVariable Long id) {
        StockResponse clientResponse = service.findAllStock(StockFilterRequest.builder().id(id).build()).getContent().get(0);
        AppResponse<StockResponse> response = AppResponse.<StockResponse>builder().message(AppConstants.ApiResponseMessage.SUCCESSFUL)
                .status(HttpStatus.OK.value()).data(clientResponse).error("").build();
        return ResponseEntity.ok().body(response);
    }
}
