package com.supplyhouse.assignment.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * This is entity class and it is mapped with INVITATION table.
 * it stores sub account invitations data
 * */
@Entity
@Getter
@Setter
@Table(name="INVITATION")
@NoArgsConstructor
public class Invitation {

    public Invitation(String parentAccountId, String subAccountId) {
        this.parentAccountId = parentAccountId;
        this.subAccountId = subAccountId;
    }

    /**
     * Auto generated primary key value of invitation
     * */
    @Id
    @Column(name = "invitation_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long invitationId;

    /**
     * Business account id which has sent the invitation
     * */
    @Column(name = "parent_account_id", nullable = false)
    private String parentAccountId;

    /**
     * Account id to whom invitation sent
     * */
    @Column(name = "sub_account_id", nullable = false)
    private String subAccountId;

    /**
     * Invitation sent date
     * */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="INVITATION_DATE", nullable = false)
    private Date invitationDate = new Date();

    /**
     * Invitation status change data in case of invitation acceptance or rejection
     * */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="INVITATION_STATUS_CHANGE_DATE", nullable = false)
    private Date invitationStatusChangeDate = new Date();

    /**
     * Current invitation status
     * */
    @Column(name = "INVITATION_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus = InvitationStatus.NO_ACTION;

}
