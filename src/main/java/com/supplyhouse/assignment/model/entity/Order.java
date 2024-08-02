package com.supplyhouse.assignment.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Date;


/**
 * This is a entity class and it is mapped with ORDER_DETAIL table
 * It stores order related data.
 * */
@Entity
@Table(name="ORDER_DETAIL")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    public Order(String orderId, Account account) {
        this.orderId = orderId;
        this.account = account;
    }

    /**
     * This is primary key param of order
     * */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * Account id which has created this order
     * */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_id")
    @JsonIgnore
    private Account account;

    /**
     * Date of order created
     * */
    @Column(name="order_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate = new Date();

     // private List<OrderItem> orderItemList;
}
