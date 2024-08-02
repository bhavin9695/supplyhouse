package com.supplyhouse.assignment.model.DTO;

import com.supplyhouse.assignment.model.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * This class hold request or response data of account management apis.
 * */
@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    @NotBlank
    private String accountId;
    @NotBlank
    private String firstname;
    private String lastname;
    private Date creationDate;
    private AccountType accountType;

    public AccountDTO(String accountId, String firstname, String lastname, Date creationDate, AccountType accountType) {
        this.accountId = accountId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.creationDate = creationDate;
        this.accountType = accountType;
    }
}
