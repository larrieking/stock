package com.acl.stock.domain.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PagedResponse<T> {

    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;


    @Builder.Default
    private boolean last = Boolean.FALSE;


    @Builder.Default
    private boolean first = Boolean.FALSE;

    public PagedResponse() {

    }

    public PagedResponse(List<T> content, int number, int size, long totalElements, int totalPages, boolean last, boolean first) {
        this.content = content;
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
        this.first = first;
    }

    public PagedResponse(Page<T> results) {
        this.content = results.getContent();
        this.number = results.getNumber();
        this.size = results.getSize();
        this.totalElements = results.getNumberOfElements();
        this.totalPages = results.getTotalPages();
        this.last = results.isLast();
        this.first = results.isFirst();
    }
}
