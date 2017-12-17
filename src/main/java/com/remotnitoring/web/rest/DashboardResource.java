package com.remotnitoring.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.codahale.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.repository.HeartbeatRepository;
import com.remotnitoring.service.dto.MonitorNodeDTO;

/**
 * Dashboard controller
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);
    
    HeartbeatRepository heartbeatRepository;
    
    public DashboardResource(HeartbeatRepository heartbeatRepository) {
		super();
		this.heartbeatRepository = heartbeatRepository;
	}

	/**
    * GET lastSituation
    */
    @GetMapping("/last-situation")
    @Timed
    public ResponseEntity<List<MonitorNodeDTO>> lastSituation() {
        log.debug("REST request to get last-situation Heartbeats per Node");

    		List<MonitorNodeDTO> lMonitorNodeDTO =  heartbeatRepository.countAllHeartBeatsPerNode();
    		
    		
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-MonitorNodeDTO-Total-Count", Long.toString(lMonitorNodeDTO.size()));        
        return new ResponseEntity<>(lMonitorNodeDTO, headers, HttpStatus.OK);

    }

}
