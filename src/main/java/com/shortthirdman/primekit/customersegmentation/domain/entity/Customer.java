package com.shortthirdman.primekit.customersegmentation.domain.entity;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Builder
public class Customer {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private BigDecimal totalSpent;

    @Column
    private CustomerTier tier;
}
