package com.remotnitoring.repository;

import com.remotnitoring.domain.RequestRemoteCommand;
import com.remotnitoring.domain.ResponseRemoteCommand;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ResponseRemoteCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponseRemoteCommandRepository extends JpaRepository<ResponseRemoteCommand, Long> {

	
	@Query("select responseRemoteCommand from ResponseRemoteCommand responseRemoteCommand "
			+ " where responseRemoteCommand.requestRemoteCommand.node.user.login = ?#{principal.username} "
			+ "   and responseRemoteCommand.requestRemoteCommand.id=?1")
	Page<ResponseRemoteCommand> findByIdRequestAndIsCurrentUser(Long idRequest, Pageable pageable);
	
}
