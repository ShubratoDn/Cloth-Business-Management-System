package com.cloth.business.entities;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class TradeTransactionDetails { // Changed class name from PurchaseDetails to TradeTransactionDetails

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
    
    private String image;

    @Transient
    private MultipartFile productImage;

    @JsonIgnore
    public MultipartFile getProductImage() {
        return productImage;
    }

    @JsonProperty
    public void setProductImage(MultipartFile productImage) {
        this.productImage = productImage;
    }
    
    @ManyToOne
    @JoinColumn(name = "trade_transaction_id", nullable = false) // Changed from purchase_id to trade_transaction_id
    @ToString.Exclude
    private TradeTransaction tradeTransaction; // Changed type from Purchase to TradeTransaction

    @JsonIgnore
    public TradeTransaction getTradeTransaction() { // Changed method to return TradeTransaction
        return tradeTransaction;
    }

    @JsonProperty
    public void setTradeTransaction(TradeTransaction tradeTransaction) { // Changed parameter type to TradeTransaction
        this.tradeTransaction = tradeTransaction;
    }
}
