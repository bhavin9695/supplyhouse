package com.supplyhouse.assignment.DAO;

import com.supplyhouse.assignment.model.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This is repository class which handles the account_detail tables related database operations.
 * */
@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    Account findByAccountId(String accountId);

}
