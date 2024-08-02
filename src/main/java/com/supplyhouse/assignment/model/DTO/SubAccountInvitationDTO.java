package com.supplyhouse.assignment.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * This class holds request / response detail for sub account invitations
 * */
@Getter
@Setter
public class SubAccountInvitationDTO {
    @NotBlank
    private Long invitationId;
    @NotBlank
    private SubAccountJoiningFrom subAccountJoiningFrom;
}
