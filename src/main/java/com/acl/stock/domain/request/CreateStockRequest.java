package com.acl.stock.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateStockRequest {
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "current price is required")
    private BigDecimal currentPrice;
}
