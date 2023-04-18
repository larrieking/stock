package com.acl.stock.domain.response;

import lombok.Data;

public abstract class AuditableResponse {

    protected String createdBy;
    protected String createdDate;
    protected String lastModifiedBy;
    protected String lastModifiedDate;
    protected Long version;
    protected Integer status;
    protected Integer authorized;
}
