package com.supplyhouse.assignment.service;

import com.supplyhouse.assignment.DAO.OrderRepository;
import com.supplyhouse.assignment.model.DTO.OrderDTO;
import com.supplyhouse.assignment.model.entity.Account;
import com.supplyhouse.assignment.model.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This service class holds the business logic of order management request processing
 * */
@Service
public class OrderServiceImpl implements OrderService{

    private final AccountService accountServiceImpl;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository, AccountService accountServiceImpl) {
        this.orderRepository = orderRepository;
        this.accountServiceImpl = accountServiceImpl;
    }

    /**
     * This method process the new order request
     * @param accountId by which order is placed
     * @param orderDTO order details
     * */
    @Override
    public Order placeOrder(String accountId, OrderDTO orderDTO) {
        Account account = accountServiceImpl.getAccountDetail(accountId);
        return this.orderRepository.save(new Order(orderDTO.getOrderId(), account));
    }

    /**
     * This method provide the order history for the requested account id
     * @param accountId for which order history requested
     * */
    @Override
    public List<OrderDTO> getOrderHistory(String accountId) {
        List<Order> orderList = orderRepository.findAllByAccountId(accountId);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderList.forEach(order -> {
            orderDTOList.add(new OrderDTO(order.getOrderId(), order.getAccount().getAccountId(), order.getOrderDate()));
        });
        return orderDTOList;
    }
}
