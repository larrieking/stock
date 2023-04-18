package com.acl.stock.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockResponse{
    private Long id;


    private String name;

    private String currentPrice;

    private Date createdDate;
    private Date lastModifiedDate;
}
