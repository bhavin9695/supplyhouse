package com.supplyhouse.assignment.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * This class holds order details for order management related APIs
 * */
@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    @NotBlank
    private String orderId;
    private String byAccountId;
    private Date orderDate;
    //private List<OrderItem> items;


    public OrderDTO(String orderId, String byAccountId, Date orderDate) {
        this.orderId = orderId;
        this.byAccountId = byAccountId;
        this.orderDate = orderDate;
    }
}
