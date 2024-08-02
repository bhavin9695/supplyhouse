package com.supplyhouse.assignment.service;

import com.supplyhouse.assignment.model.DTO.OrderDTO;
import com.supplyhouse.assignment.model.entity.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(String accountId, OrderDTO orderDTO);
    List<OrderDTO> getOrderHistory(String accountId);
}
