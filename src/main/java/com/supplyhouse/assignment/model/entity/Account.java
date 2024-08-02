package com.supplyhouse.assignment.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

/**
 * This is entity class and it is mapped with ACCOUNT_DETAIL table.
 * it holds the account detail related data.
 * */
@Entity
@Table(name="ACCOUNT_DETAIL")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    /**
     * This is primary key field.
     * */
    @Id
    @Column(name = "account_id")
    private String accountId;

    /**
     * Store user's first name
     * */
    @Column(name="first_name", length=50, nullable=false)
    private String firstname;

    /**
     * Store user's last name
     * */
    @Column(name="last_name", length=50)
    private String lastname;

    /**
     * This is foreign key mapping of orders made by this account.
     * Data will be fetched lazily
     * */
    @OneToMany(mappedBy= "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Order> orders;

    /**
     * If a account is type of sub account then this param indicates the business account id
     * */
    @Column(name = "parent_account_id")
    private String parentAccountId;

    /**
     * This param stores date from which order history can be shared.
     * */
    @Column(name = "sub_account_history_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subAccHistoryFrom;

    /**
     * Account creation date
     * */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_date", nullable = false)
    private Date creationDate = new Date();

    /**
     * Type of account.
     * Account can be type of either INDIVIDUAL, BUSINESS, SUBACCOUNT
     * */
    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType = AccountType.INDIVIDUAL;

    public Account(String accountId, String firstname, String lastname) {
        this.accountId = accountId;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
