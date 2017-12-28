package com.remotnitoring.repository;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.domain.Node;
import com.remotnitoring.service.dto.MonitorNodeDTO;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
			+ " (SELECT count(heartbeat) FROM Heartbeat heartbeat WHERE heartbeat.node = node AND heartbeat.timestamp > ?1 ) as total, "	
			+ " (SELECT max (heartbeat.timestamp) FROM Heartbeat heartbeat WHERE heartbeat.node = node AND heartbeat.timestamp > ?1) as maxTimestamp "
			+ ") " 
		    + "FROM Node node "
		  )	
	List<MonitorNodeDTO> countLastHeartBeatsPerNode(ZonedDateTime timestamp);
	
	
	@Query(value = 
			"SELECT new com.remotnitoring.service.dto.MonitorNodeDTO "
			+ "(heartbeat.node.id, "
			+ " heartbeat.node.name, "
			+ " 1L, "	
			+ " heartbeat.timestamp "
			+ ") " 
		    + " FROM Heartbeat heartbeat "
		    + " WHERE heartbeat.timestamp > ?1 AND heartbeat.node = ?2 "
		    + " ORDER BY heartbeat.timestamp  "
		  )	
	List<MonitorNodeDTO> findLastestMonitorByNodeOrderByTimestamp(ZonedDateTime timestamp, Node node);

	
	List <Heartbeat> findByTimestampBetweenAndNodeOrderByTimestampDesc (ZonedDateTime fromDate, ZonedDateTime toDate, Node node);

	//void deleteByTimestampLessThan(ZonedDateTime minusDays);
}
