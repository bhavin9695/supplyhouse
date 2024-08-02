package com.supplyhouse.assignment.controller;

import com.supplyhouse.assignment.model.DTO.AccountDTO;
import com.supplyhouse.assignment.model.DTO.SubAccountInvitationDTO;
import com.supplyhouse.assignment.model.entity.Account;
import com.supplyhouse.assignment.model.entity.Invitation;
import com.supplyhouse.assignment.model.entity.InvitationStatus;
import com.supplyhouse.assignment.service.AccountService;
import com.supplyhouse.assignment.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * This controller class handles account management related APIs.
 * */

@RestController
@RequestMapping("/account")
public class AccountController {

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountServiceImpl;

    private final AuthenticationService authenticationService;

    public AccountController(AccountService accountServiceImpl, AuthenticationService authenticationService) {
        this.accountServiceImpl = accountServiceImpl;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Create new account",
            tags = "Account Management",
            description = "Using this API you can create a new account. By default account type will be individual"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description =
                    "This api accepts the account id, firstname and optional lastname.",
            required = true,
            content =
            @Content(
                    schema = @Schema(implementation = AccountDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name = "Create new account by account id and firstname",
                                    value = "{\n" +
                                            "  \"accountId\": \"newaccount@gmai.com\",\n" +
                                            "  \"firstname\": \"New account Firstname\"\n" +
                                            "}"),
                            @ExampleObject(
                                    name = "Create new account by account id, firstname and lastname",
                                    value = "{\n" +
                                            "  \"accountId\": \"newaccount@gmai.com\",\n" +
                                            "  \"firstname\": \"New account Firstname\",\n" +
                                            "  \"lastname\": \"New account Lastname\"\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account created successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation =
                                            AccountDTO.class))
                    )
            })
    @PostMapping(value = "/create")
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO) {
        Account account = accountServiceImpl.createAccount(accountDTO);
        AccountDTO accountResponseDTO = new AccountDTO(account.getAccountId(),
                account.getFirstname(),
                account.getLastname(),
                account.getCreationDate(),
                account.getAccountType());
        return new ResponseEntity<>(accountResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Upgrade individual account to business account",
            tags = "Account Management",
            description = "When logged in user request for the account upgrade," +
                    " it takes the account id of logged in user and checks the number of order." +
                    " if number of order are more than 10 then it convert the account to Business account."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Operation")
            })
    @PostMapping(value = "/upgrade")
    public ResponseEntity<?> upgradeToBusinessAccount() {
        String accountId = authenticationService.getLoggedInUserAccountId();
        return new ResponseEntity<>(accountServiceImpl.upgradeToBusinessAccount(accountId), HttpStatus.OK);
    }


    @Operation(summary = "Send request for joining as sub account",
            tags = "Account Management",
            description = "Business account holder can send invitation to join as sub account to another account id." +
                    " Invited account holder can either accept or reject the invitation."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Invitation sent successfully."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Either your account is not type of business \n or " +
                                    "provided sub account id is associated with other business account."
                    )
            })
    @PostMapping("/subaccount/invitation/{subAccountId}")
    public ResponseEntity<?> sendSubAccountInvitation(@PathParam("subAccountId") String subAccountId) {
        String parentAccountId = authenticationService.getLoggedInUserAccountId();
        accountServiceImpl.sendSubAccountInvitation(parentAccountId, subAccountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all invitations of joining as sub account",
            tags = "Account Management",
            description = "User can get the list of all the invitation which he/she has received."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Invitations list retrieved successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation =
                                            Invitation[].class))
                    )
            })
    @GetMapping("/subaccount/invitation")
    public ResponseEntity<?> getSubAccountInvitation() {
        String accountId = authenticationService.getLoggedInUserAccountId();
        List<Invitation> subAccountInvitations = accountServiceImpl.getSubAccountInvitationsByStatus(accountId, InvitationStatus.NO_ACTION);
        return new ResponseEntity<>(subAccountInvitations, HttpStatus.OK);
    }


    @Operation(summary = "Accept the sub account invitation",
            tags = "Account Management",
            description = "Using this API user can accept the invitation of joining as a sub account"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description =
                    "This api accept the invitation id and enum value 'FROM_CREATION' or 'FROM_INVITATION_ACCEPTANCE' to indicate from which date user wants share order history",
            required = true,
            content =
            @Content(
                    schema = @Schema(implementation = SubAccountInvitationDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name =
                                            "Accept the invitation and share history from the date of account creation",
                                    value = "{\n" +
                                            "  \"invitationId\": 0,\n" +
                                            "  \"subAccountJoiningFrom\": \"FROM_CREATION\"\n" +
                                            "}"),
                            @ExampleObject(
                                    name =
                                            "Accept the invitation and share history from the date of joining as sub account",
                                    value = "{\n" +
                                            "  \"invitationId\": 0,\n" +
                                            "  \"subAccountJoiningFrom\": \"FROM_INVITATION_ACCEPTANCE\"\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Invitation accepted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Either null or invalid invitation id provided \n" +
                                    "or Account is already a sub account"
                    )
            })
    @PostMapping("/subaccount/invitation/accept")
    public ResponseEntity<?> acceptSubAccountInvitation(@RequestBody SubAccountInvitationDTO subAccountInvitationDTO) {
        String loggedInUserAccountId = authenticationService.getLoggedInUserAccountId();
        accountServiceImpl.acceptSubAccountInvitation(loggedInUserAccountId, subAccountInvitationDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Reject the sub account invitation",
            tags = "Account Management",
            description = "Using this API user can reject the invitation of joining as a sub account"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description =
                    "This api accept the invitation id",
            required = true,
            content =
            @Content(
                    schema = @Schema(implementation = SubAccountInvitationDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name =
                                            "Reject invitation",
                                    value = "{\n" +
                                            "  \"invitationId\": 0\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Invitation rejected successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Either null or invalid invitation id provided \n" +
                                    "or Account is already a sub account"
                    )
            })
    @PostMapping("/subaccount/invitation/reject")
    public ResponseEntity<?> rejectSubAccountInvitation(@RequestBody SubAccountInvitationDTO subAccountInvitationDTO) {
        String loggedInUserAccountId = authenticationService.getLoggedInUserAccountId();
        accountServiceImpl.rejectSubAccountInvitation(loggedInUserAccountId, subAccountInvitationDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Unlink sub account by self",
            tags = "Account Management",
            description = "Using this api user can unlink self from associated business account."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Account unlinked successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Account is not a type of sub account"
                    )
            })
    @PutMapping("/subaccount/unlink")
    public ResponseEntity<?> unlinkSubAccountFromBusinessAccount() {
        String loggedInUserAccountId = authenticationService.getLoggedInUserAccountId();
        accountServiceImpl.unlinkSubAccountFromBusinessAccountBySelf(loggedInUserAccountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Unlink sub account by business account holder",
            tags = "Account Management",
            description = "Using this api a business account holder can unlink their sub accounts."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Account unlinked successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Your account is not a type of business account\n " +
                                    "or You can only unlink sub account associated with your business account"
                    )
            })
    @PutMapping("/subaccount/unlink/{subAccountId}")
    public ResponseEntity<?> unlinkSubAccountFromBusinessAccount(@PathParam("subAccountId") String subAccountId) {
        String loggedInUserAccountId = authenticationService.getLoggedInUserAccountId();
        accountServiceImpl.unlinkSubAccountFromBusinessAccountByParent(loggedInUserAccountId, subAccountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
