package com.supplyhouse.assignment.DAO;

import com.supplyhouse.assignment.model.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This is repository class which handles the order_detail tables related database operations.
 * */
public interface OrderRepository extends CrudRepository<Order, String> {

    @Query("select o from Order o where o.account.accountId = :accountId " +
            "union" +
            " select o1 from Order o1 join Account a1 on o1.account.accountId = a1.accountId " +
            " where a1.parentAccountId = :accountId and o1.orderDate >= a1.subAccHistoryFrom")
    List<Order> findAllByAccountId(@Param("accountId") String accountId);
}
