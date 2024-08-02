package com.supplyhouse.assignment.service;


import com.supplyhouse.assignment.model.DTO.AccountDTO;
import com.supplyhouse.assignment.model.DTO.SubAccountInvitationDTO;
import com.supplyhouse.assignment.model.entity.Account;
import com.supplyhouse.assignment.model.entity.Invitation;
import com.supplyhouse.assignment.model.entity.InvitationStatus;

import java.util.List;

public interface AccountService {
    Account getAccountDetail(String accountId);
    boolean upgradeToBusinessAccount(String accountId);
    void sendSubAccountInvitation(String parentAccountId, String subAccountId) throws IllegalArgumentException;
    List<Invitation> getSubAccountInvitationsByStatus(String accountId, InvitationStatus invitationStatus);
    void acceptSubAccountInvitation(String loggedInAccountId, SubAccountInvitationDTO subAccountInvitationDTO) throws IllegalArgumentException, IllegalStateException;
    void rejectSubAccountInvitation(String loggedInAccountId, SubAccountInvitationDTO subAccountInvitationDTO) throws IllegalArgumentException, IllegalStateException;
    void unlinkSubAccountFromBusinessAccountBySelf(String subAccountId) throws IllegalStateException;
    void unlinkSubAccountFromBusinessAccountByParent(String parentAccountId, String subAccountId) throws IllegalArgumentException, IllegalStateException;
    Account createAccount(AccountDTO accountDTO);

}
