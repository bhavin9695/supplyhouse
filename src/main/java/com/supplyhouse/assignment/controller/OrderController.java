package com.supplyhouse.assignment.controller;

import com.supplyhouse.assignment.model.DTO.OrderDTO;
import com.supplyhouse.assignment.model.DTO.SubAccountInvitationDTO;
import com.supplyhouse.assignment.model.entity.Invitation;
import com.supplyhouse.assignment.model.entity.Order;
import com.supplyhouse.assignment.service.AuthenticationService;
import com.supplyhouse.assignment.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderServiceImpl;

    private final AuthenticationService authenticationService;

    public OrderController(OrderService orderServiceImpl, AuthenticationService authenticationService) {
        this.orderServiceImpl = orderServiceImpl;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Place a new order",
            tags = "Order Management",
            description = "Using this API user can place a new order."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description =
                    "This api accepts the order request.",
            required = true,
            content =
            @Content(
                    schema = @Schema(implementation = OrderDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name = "Place a new order",
                                    value ="{\n" +
                                            "  \"orderId\": \"order-12234\"\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order placed successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation =
                                            OrderDTO.class))
                    )
            })
    @PostMapping()
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO orderDTO){
        String loggedInUserAccountId = authenticationService.getLoggedInUserAccountId();
        Order order = orderServiceImpl.placeOrder(loggedInUserAccountId, orderDTO);
        OrderDTO orderResponseDTO = new OrderDTO(order.getOrderId(), order.getAccount().getAccountId(), order.getOrderDate());
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get order history",
            tags = "Order Management",
            description = "Get order history of the account"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order placed successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation =
                                            OrderDTO[].class))
                    )
            })
    @GetMapping("/history")
    public ResponseEntity<?> getOrderHistory(){
        String loggedInUserAccountId = authenticationService.getLoggedInUserAccountId();
        List<OrderDTO> orderDTOList = orderServiceImpl.getOrderHistory(loggedInUserAccountId);
        return new ResponseEntity<>(orderDTOList, HttpStatus.OK);
    }
}
