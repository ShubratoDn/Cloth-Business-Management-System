package com.cloth.business.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class PurchaseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    private Product product;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @Column(nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    private String remark;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
//    @ToString.Exclude
    private Purchase purchase;
}
