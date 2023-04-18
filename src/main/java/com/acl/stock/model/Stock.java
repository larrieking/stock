package com.acl.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@SuperBuilder
@Table(name = "STOCK")
@EqualsAndHashCode(callSuper = true)
public class Stock extends Auditable<String> implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;


    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CURRENT_PRICE", nullable = false)
    private String currentPrice;


}
