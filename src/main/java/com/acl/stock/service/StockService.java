package com.acl.stock.service;

import com.acl.stock.domain.request.PaginationRequest;
import com.acl.stock.domain.request.StockFilterRequest;
import com.acl.stock.domain.response.PagedResponse;
import com.acl.stock.domain.response.StockResponse;
import com.acl.stock.model.Stock;
import com.acl.stock.repository.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@SuppressWarnings("unchecked")
@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {


    private final BaseRepository repo;
    private final ModelMapper mapper;
    private final ObjectMapper objectMapper;


    public PagedResponse<StockResponse>findAllStock(StockFilterRequest request){
        Map<String, Object> filter = objectMapper.convertValue(request, Map.class);
        Page<Stock>stocks = repo.findAllBy(Stock.class, filter, PaginationRequest.builder().page(request.getPage()).size(request.getSize()).build());
        return mapper.map(stocks, new TypeToken<PagedResponse<StockResponse>>(){}.getType());
    }


}
