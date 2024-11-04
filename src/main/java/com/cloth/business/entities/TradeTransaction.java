package com.cloth.business.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cloth.business.entities.enums.TransactionStatus; // Changed enum import

@Data
@Entity
@ToString
public class TradeTransaction { // Changed class name from Purchase to TradeTransaction

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_number") // Changed attribute name from poNumber to transactionNumber
    private String transactionNumber;
    
    @ManyToOne
    @NotNull(message = "Store is required")
    private Store store;
    
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false) // Changed from supplier_id to partner_id
    @NotNull(message = "Partner is required") // Updated validation message
    private StakeHolder partner; // Changed attribute name from supplier to partner

    @ManyToOne
    @JoinColumn(name = "processed_by_id", nullable = false) // Changed from added_by_id to processed_by_id
    private User processedBy; // Changed attribute name from addedBy to processedBy

    private Double totalAmount;

    private Double discountAmount;    
    private String discountRemark;

    private Double chargeAmount; // Changed from chargeAmount to additionalChargeAmount
    private String chargeRemark; // Changed from chargeRemark to additionalChargeRemark
  
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Ensure this matches the format you're using in the frontend
    private Date transactionDate; // Changed attribute name from purchaseDate to transactionDate

    @OneToMany(mappedBy = "tradeTransaction", cascade = CascadeType.ALL) // Changed mappedBy from purchase to tradeTransaction
    private List<TradeTransactionDetails> transactionDetails; // Changed type from PurchaseDetails to TradeTransactionDetails

    private String remark;
    
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus; // Changed enum from PurchaseStatus to TransactionStatus

    @ManyToOne
    private User approvedBy;
    
    private Date approvedDate;
    
    private Date timestamp;
    
    @ManyToOne
    private User lastUpdatedBy;
    
    private Date lastUpdatedDate;
    
    @ManyToOne
    private User rejectedBy;
    
    private Date rejectedDate;
    
    @Column(length = 3000)
    private String rejectedNote;
}
