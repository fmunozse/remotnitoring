package com.remotnitoring.repository;

import com.remotnitoring.domain.Node;
import com.remotnitoring.domain.RequestRemoteCommand;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RequestRemoteCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestRemoteCommandRepository extends JpaRepository<RequestRemoteCommand, Long> {

	
	@Query("select requestRemoteCommand from RequestRemoteCommand requestRemoteCommand "
			+ " where requestRemoteCommand.node.user.login = ?#{principal.username} "
			+ "   and requestRemoteCommand.status='Pending' ")
	List<RequestRemoteCommand> findByUserIsCurrentUser();
	
	@Query("select requestRemoteCommand from RequestRemoteCommand requestRemoteCommand "
			+ " where requestRemoteCommand.node.user.login = ?#{principal.username} "
			+ "   and requestRemoteCommand.status='Pending' "
			+ "   and requestRemoteCommand.id = ?1 ")	
	Optional<RequestRemoteCommand> findByUserAndIdAndPending (Long id);
}
