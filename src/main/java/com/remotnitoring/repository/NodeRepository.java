package com.remotnitoring.repository;

import com.remotnitoring.domain.Node;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Node entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {

	@Query("select node from Node node where node.user.login = ?#{principal.username}")
	Optional<Node> findByUserIsCurrentUser();

	
}
