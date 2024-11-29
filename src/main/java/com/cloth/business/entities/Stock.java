package com.cloth.business.entities;

import com.cloth.business.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@ToString
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;
    
    @ManyToOne
    private TradeTransaction transaction;

    @ManyToOne
    private TradeTransactionDetails transactionDetasils;

    private String location; 
    
    private String remark;
    
    @ManyToOne
    private Store store;

    private Date timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
