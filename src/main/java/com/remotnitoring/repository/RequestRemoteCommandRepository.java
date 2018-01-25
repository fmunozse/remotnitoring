package com.remotnitoring.repository;

import com.remotnitoring.domain.RequestRemoteCommand;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RequestRemoteCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestRemoteCommandRepository extends JpaRepository<RequestRemoteCommand, Long> {

}
