package com.supplyhouse.assignment.DAO;


import com.supplyhouse.assignment.model.entity.Invitation;
import com.supplyhouse.assignment.model.entity.InvitationStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * This is repository class which handles the invitation tables related database operations.
 * */
@Repository
public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    List<Invitation> findBySubAccountIdAndInvitationStatus(String subAccountId, InvitationStatus invitationStatus);
}
