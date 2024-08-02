package com.supplyhouse.assignment.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is used for passing accountId value in login api request
 * */
@Getter
@Setter
public class LoginDTO {
    @NotBlank
    private String loginByAccountId;
}
