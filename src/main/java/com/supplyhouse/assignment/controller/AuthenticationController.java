package com.supplyhouse.assignment.controller;

import com.supplyhouse.assignment.model.DTO.AccountDTO;
import com.supplyhouse.assignment.model.DTO.LoginDTO;
import com.supplyhouse.assignment.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;

/**
 * This controller handles the account authentication
 *
 * NOTE : This is created for the purpose of storing login account id
 * so that account id value can be used in other APIs.
 * Actual implementation of authentication and authorization should be handled
 * through spring security services
 * */
@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Login by account id",
            tags = "Login Management",
            description = "Using this API you can login with your account id. \n NOTE : This API does not do any actual validation except provided account id exist.\nThis is only for the purpose of setting logged user account id so that it can be used in other APIs."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description =
                    "This api accepts the account id by which you want to do login",
            required = true,
            content =
            @Content(
                    schema = @Schema(implementation = AccountDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name = "Login by account id",
                                    value ="{\n" +
                                            "  \"loginByAccountId\": \"owner@business.com\"\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logged in successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema =
                                    @Schema(implementation =
                                            String.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized access. Provided account id is invalid"
                    )
            })
    @PostMapping()
    public ResponseEntity<?> login(@RequestBody LoginDTO data){
        try {
            return new ResponseEntity<>(authenticationService.doLogin(data), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

}
