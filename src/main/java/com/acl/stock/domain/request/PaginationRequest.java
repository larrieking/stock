package com.acl.stock.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationRequest {

    @Builder.Default
    @Min(value = 1, message = "Page must be greater than zero (0)")
    @JsonIgnore
    private Integer page = 1;

    @Builder.Default
    @JsonIgnore
    @Max(value = 100, message = "Page size must be greater than 0 but less than 100")
    private Integer size = 25;
}
