package com.acl.stock.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@DynamicUpdate
public abstract class Auditable<U> implements Serializable {

    /**
     *
     */

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    protected Date createdDate;
    /**
     * U lastModifiedBy
     */

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
    protected Date lastModifiedDate;

}
