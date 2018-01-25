package com.remotnitoring.repository;

import com.remotnitoring.domain.ResponseRemoteCommand;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ResponseRemoteCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponseRemoteCommandRepository extends JpaRepository<ResponseRemoteCommand, Long> {

}
