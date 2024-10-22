package com.cloth.business.payloads;

import lombok.Data;

@Data
public class ReportProductDetails {

    private Long itemId;

    private String itemName;
    
    private String itemCategory;
    
    private Integer itemQuantity;
    
    private String itemUOM;
    
    private Double itemPrice ;
    
    private Double itemTotal;
    
    

}
