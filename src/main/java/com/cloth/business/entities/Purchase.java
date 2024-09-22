package com.cloth.business.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cloth.business.entities.enums.PurchaseStatus;

@Data
@Entity
@ToString
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "po_number")
    private String poNumber;
    
    @ManyToOne
    @NotNull(message = "Store is required")
    private Store store;
    
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    @NotNull(message = "Supplier is required")
    private StakeHolder supplier;

    @ManyToOne
    @JoinColumn(name = "added_by_id", nullable = false)
    private User addedBy;

    private Double totalAmount;

  
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Ensure this matches the format you're using in the frontend
    private Date purchaseDate;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseDetails> purchaseDetails;

    private String remark;
    
    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;
    
    private User approvedBy;
    
    private Date approvedDate;
    
    private Date timestamp;
    
    private User lastUpdatedBy;
    
    private Date lastUpdatedDate;
}
