package com.supplyhouse.assignment.service;

import com.supplyhouse.assignment.DAO.AccountRepository;
import com.supplyhouse.assignment.DAO.InvitationRepository;
import com.supplyhouse.assignment.model.DTO.AccountDTO;
import com.supplyhouse.assignment.model.DTO.SubAccountInvitationDTO;
import com.supplyhouse.assignment.model.DTO.SubAccountJoiningFrom;
import com.supplyhouse.assignment.model.entity.Account;
import com.supplyhouse.assignment.model.entity.AccountType;
import com.supplyhouse.assignment.model.entity.Invitation;
import com.supplyhouse.assignment.model.entity.InvitationStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This service class holds the business logic of account management request processing
 * */

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final InvitationRepository invitationRepository;

    AccountServiceImpl(AccountRepository accountRepository,
                       InvitationRepository invitationRepository) {
        this.accountRepository = accountRepository;
        this.invitationRepository = invitationRepository;

    }

    /**
     * This service method fetches the account detail by account id
     * @param accountId account id for which you want to fetch detail
     * @return Object of Account detail
     * */
    @Override
    public Account getAccountDetail(String accountId){
        return accountRepository.findByAccountId(accountId);
    }

    /**
     * This method process the account upgradation request.
     *
     * @param accountId account id for upgradation request received.
     * @return true or false
     * @throws IllegalArgumentException when Account is either already a business account or sub account
     * */
    @Override
    public boolean upgradeToBusinessAccount(String accountId) {
        Account account = getAccountDetail(accountId);
        if(!AccountType.INDIVIDUAL.equals(account.getAccountType())){
            throw new IllegalStateException("Account is either already a business account or sub account");
        }
        if (account.getOrders().size() >= 10) {
            account.setAccountType(AccountType.BUSINESS);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    /**
     * This method process the sub account invitations.
     *
     * @param subAccountId account id for which request received
     * @param parentAccountId account id by which request sent
     * @throws IllegalArgumentException in case of invalid sub account id, parentAccountId is not a type of business account
     * */
    @Override
    public void sendSubAccountInvitation(String parentAccountId, String subAccountId) throws IllegalArgumentException {
        Account subAccount = getAccountDetail(subAccountId);
        if (ObjectUtils.isEmpty(subAccount)) {
            throw new IllegalArgumentException("Invalid sub account id");
        }
        if(AccountType.SUBACCOUNT.equals(subAccount.getAccountType())){
            throw new IllegalArgumentException("Account is already a sub account");
        }
        Account parentAccount = getAccountDetail(parentAccountId);
        if(!AccountType.BUSINESS.equals(parentAccount.getAccountType())){
            throw new IllegalArgumentException("You can not send invitation of sub account as your account is not type of business account");
        }
        invitationRepository.save(new Invitation(parentAccountId, subAccountId));
    }

    /**
     * Fetch the sub account invitation by account id and invitation status
     * @param accountId account id for which you want to fetch the invitations
     * @param invitationStatus type of invitation status for which you want to get the detail
     * */
    @Override
    public List<Invitation> getSubAccountInvitationsByStatus(String accountId, InvitationStatus invitationStatus) {
        return invitationRepository.findBySubAccountIdAndInvitationStatus(accountId, invitationStatus);
    }

    /**
     * Accepts the invitation sent to provided account id.
     *
     * @param accountId account id of a user who is accepting the invitation
     * @param subAccountInvitationDTO invitation acceptance request detail
     * @throws IllegalArgumentException in case of either null or invalid invitation details
     * @throws IllegalStateException in case of account is already sub account type
     *
     * */
    @Override
    @Transactional
    public void acceptSubAccountInvitation(String accountId, SubAccountInvitationDTO subAccountInvitationDTO) throws IllegalArgumentException, IllegalStateException {
        Optional<Invitation> invitationById = invitationRepository.findById(subAccountInvitationDTO.getInvitationId());
        // validate the invitation with logged in user
        if (invitationById.isEmpty()
                || !invitationById.get().getSubAccountId().equals(accountId)) {
            throw new IllegalArgumentException("Either null or invalid invitation id provided");
        }

        // validate and change from individual account to sub account and update the sub account history visible from
        Account subAccount = getAccountDetail(accountId);
        if (!AccountType.INDIVIDUAL.equals(subAccount.getAccountType())) {
            throw new IllegalStateException("Account is already a sub account");
        }

        subAccount.setParentAccountId(invitationById.get().getParentAccountId());
        subAccount.setAccountType(AccountType.SUBACCOUNT);
        if (SubAccountJoiningFrom.FROM_INVITATION_ACCEPTANCE.equals(subAccountInvitationDTO.getSubAccountJoiningFrom())) {
            subAccount.setSubAccHistoryFrom(new Date());
        } else {
            subAccount.setSubAccHistoryFrom(subAccount.getCreationDate());
        }
        accountRepository.save(subAccount);

        // change status of invitations of sub account id
        List<Invitation> subAccountInvitations = getSubAccountInvitationsByStatus(accountId, InvitationStatus.NO_ACTION);
        Date currentDateTime = new Date();
        subAccountInvitations.forEach(invitation -> {
            invitation.setInvitationStatus(invitation.getInvitationId().equals(subAccountInvitationDTO.getInvitationId()) ? InvitationStatus.ACCEPTED : InvitationStatus.REJECTED);
            invitation.setInvitationStatusChangeDate(currentDateTime);
        });
        invitationRepository.saveAll(subAccountInvitations);
    }

    /**
     * reject the invitation sent to provided account id
     *
     * @param accountId account id of a user who is rejecting the invitation
     * @param subAccountInvitationDTO invitation rejection request detail
     * @throws IllegalArgumentException in case of either null or invalid invitation details
     * @throws IllegalStateException in case of account is already sub account type
     *
     * */
    @Override
    public void rejectSubAccountInvitation(String accountId, SubAccountInvitationDTO subAccountInvitationDTO) throws IllegalArgumentException, IllegalStateException {
        Optional<Invitation> invitationById = invitationRepository.findById(subAccountInvitationDTO.getInvitationId());
        // validate the invitation with logged in user
        if (invitationById.isEmpty()
                || !invitationById.get().getSubAccountId().equals(accountId)) {
            throw new IllegalArgumentException("Either null or invalid invitation id provided");
        }

        // validate and change individual account to sub account
        Account subAccount = getAccountDetail(accountId);
        if (!AccountType.INDIVIDUAL.equals(subAccount.getAccountType())) {
            throw new IllegalStateException("Account is already a sub account");
        }

        // change status of invitation to reject
        invitationById.get().setInvitationStatus(InvitationStatus.REJECTED);
        invitationById.get().setInvitationStatusChangeDate(new Date());
        invitationRepository.save(invitationById.get());
    }

    /**
     * Unlink the sub account from the business account
     * @param subAccountId account id which need to be unlinked.
     * @throws IllegalStateException in case of account is not a sub account
     * */
    @Override
    public void unlinkSubAccountFromBusinessAccountBySelf(String subAccountId) throws IllegalStateException {
        Account subAccount = getAccountDetail(subAccountId);
        if (!AccountType.SUBACCOUNT.equals(subAccount.getAccountType())) {
            throw new IllegalStateException("Account is not a type of sub account");
        }

        subAccount.setParentAccountId(null);
        subAccount.setSubAccHistoryFrom(null);
        subAccount.setAccountType(AccountType.INDIVIDUAL);
        accountRepository.save(subAccount);
    }

    /**
     * Unlink the sub account by business account
     * @param parentAccountId account id which requesting unlink
     * @param subAccountId account id which need to be unlinked
     * @throws IllegalArgumentException in case sub account id is not associated with business account
     * @throws IllegalStateException in case account is not a type of business account
     * */
    @Override
    public void unlinkSubAccountFromBusinessAccountByParent(String parentAccountId, String subAccountId) throws IllegalArgumentException, IllegalStateException {
        Account parentAccount = getAccountDetail(parentAccountId);
        if (!AccountType.BUSINESS.equals(parentAccount.getAccountType())) {
            throw new IllegalStateException("Account is not a type of business account");
        }

        Account subAccount = getAccountDetail(subAccountId);
        if(!subAccount.getParentAccountId().equals(parentAccountId)){
            throw new IllegalArgumentException("You can only unlink sub account associated with your business account");
        }

        subAccount.setParentAccountId(null);
        subAccount.setSubAccHistoryFrom(null);
        subAccount.setAccountType(AccountType.INDIVIDUAL);
        accountRepository.save(subAccount);
    }

    /**
     * Service method to create a new account
     * @param accountDTO provides the account detail to be created
     * */
    @Override
    public Account createAccount(AccountDTO accountDTO) {
        return accountRepository.save(new Account(
                accountDTO.getAccountId(),
                accountDTO.getFirstname(),
                accountDTO.getLastname()));
    }
}
