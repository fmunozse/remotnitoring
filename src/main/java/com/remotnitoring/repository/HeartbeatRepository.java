package com.remotnitoring.repository;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.service.dto.MonitorNodeDTO;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Heartbeat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HeartbeatRepository extends JpaRepository<Heartbeat, Long>, JpaSpecificationExecutor<Heartbeat> {

	
/*
	@Query(value = 
			"SELECT new com.remotnitoring.service.dto.MonitorNodeDTO "
			+ "(heartbeat.node.id,"
			+ " heartbeat.node.name,"
			+ " count(heartbeat)"
			+ ") " 
		    + "FROM Heartbeat heartbeat "
		    //+ "WHERE ep.serie.id = ?2 AND ep.season=?1 "
		    + "GROUP BY heartbeat.node.id, heartbeat.node.name"
		  )*/
	
	@Query(value = 
			"SELECT new com.remotnitoring.service.dto.MonitorNodeDTO "
			+ "(node.id,"
			+ " node.name,"
			+ " (SELECT count(heartbeat) FROM Heartbeat heartbeat WHERE heartbeat.node = node) as total, "	
			+ " (SELECT max (heartbeat.timestamp) FROM Heartbeat heartbeat WHERE heartbeat.node = node) as maxTimestamp "
			//+ " SELECT count(1) FROM Heartbeat heartbeat"
			+ ") " 
		    + "FROM Node node "
		    //+ "WHERE ep.serie.id = ?2 AND ep.season=?1 "
		  )	
	List<MonitorNodeDTO> countAllHeartBeatsPerNode();
	
}
