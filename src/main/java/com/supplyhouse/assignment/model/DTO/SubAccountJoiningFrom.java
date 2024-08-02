package com.supplyhouse.assignment.model.DTO;

/**
 * This enum indicates that from which period of time sub account user wants to share order history
 * */
public enum SubAccountJoiningFrom {
    /**
     * From the date of account creation
     * */
    FROM_CREATION,
    /**
     * From the date of sub account invitation acceptance
     * */
    FROM_INVITATION_ACCEPTANCE
}
