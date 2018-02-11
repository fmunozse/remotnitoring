package com.remotnitoring.web.rest;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.codahale.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.domain.Node;
import com.remotnitoring.messaging.Channels;
import com.remotnitoring.messaging.example.Greeting;
import com.remotnitoring.messaging.example.ProducerChannel;
import com.remotnitoring.repository.HeartbeatRepository;
import com.remotnitoring.repository.NodeRepository;
import com.remotnitoring.security.SecurityUtils;
import com.remotnitoring.service.HeartbeatService;
import com.remotnitoring.service.dto.MonitorNodeDTO;
import com.remotnitoring.web.rest.errors.InternalServerErrorException;

/**
 * Monitor controller
 */
@RestController
@RequestMapping("/api/monitor")
public class MonitorResource {

    private final Logger log = LoggerFactory.getLogger(MonitorResource.class);
        
    private HeartbeatRepository heartbeatRepository;
        
    private NodeRepository nodeRepository;
        
    private MessageChannel outputEchoChannel;
    
    
    public MonitorResource(HeartbeatService heartbeatService, HeartbeatRepository heartbeatRepository, Channels channels, 
    		NodeRepository nodeRepository) {
		
    		super();
		this.heartbeatRepository = heartbeatRepository;
        this.outputEchoChannel = channels.outputEchoChannel();
        this.nodeRepository = nodeRepository;
	}

	/**
    * GET lastSituation
    */
    @GetMapping("/last-situation")
    @Timed
    public ResponseEntity<List<MonitorNodeDTO>> lastSituation() {
        log.debug("REST request to get last-situation Heartbeats per Node");
    		List<MonitorNodeDTO> lMonitorNodeDTO =  heartbeatRepository.countLastHeartBeatsPerNode(ZonedDateTime.now().minusMinutes(60));
    		
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-MonitorNodeDTO-Total-Count", Long.toString(lMonitorNodeDTO.size()));        
        return new ResponseEntity<>(lMonitorNodeDTO, headers, HttpStatus.OK);

    }
    
    
    
    @GetMapping("/lastest-monitorNode")
    @Timed
    public ResponseEntity<Map<String, List<MonitorNodeDTO>>> lastest4hMonitorNode () {    	
    		log.debug("REST lastest4hMonitorNode");
    	
    		Map<String, List<MonitorNodeDTO>> result = new LinkedHashMap();
    	
    		List<Node> lNodes = nodeRepository.findAll();
    		for (Node node : lNodes) {
			List<MonitorNodeDTO> l = heartbeatRepository.findLastestMonitorByNodeOrderByTimestamp(
					ZonedDateTime.now().minusHours(1),
					node);
			result.put(node.getName(), l);			
		}
    	    	
    		return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    
    @GetMapping("/heartbeatsByDatePerNode")
    @Timed
    public ResponseEntity<Map<String, List<Heartbeat>>> heartbeatsByDatePerNode(
            @RequestParam(value = "fromDate") LocalDate fromDate,
            @RequestParam(value = "toDate") LocalDate toDate    		
    		) {
    	
    		log.debug("REST heartbeatsByDatePerNode fromDate:{} toDate:{}" , fromDate, toDate);
    	
    		Map<String, List<Heartbeat>> result = new LinkedHashMap();
    	
    		List<Node> lNodes = nodeRepository.findAll();
    		for (Node node : lNodes) {
			List<Heartbeat> l = heartbeatRepository.findByTimestampBetweenAndNodeOrderByTimestampDesc(
					fromDate.atStartOfDay(ZoneOffset.UTC),
					toDate.atStartOfDay(ZoneOffset.UTC),
					node);
			result.put(node.getName(), l);			
		}
    	    	
    		return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    @PostMapping("/heartbeats/ping")
    @Timed
	public ResponseEntity<Void> ping(HttpServletRequest request) {
        log.debug("REST request to ping Heartbeat : {}", request);
        
        //Node node = find();
        
	    final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> 
	    		new InternalServerErrorException("Current user login not found"));        
	    final Node node = nodeRepository.findByUserIsCurrentUser().orElseThrow(() -> 
	    		new InternalServerErrorException("Current user login [" + userLogin + "] doent have a node linked") );

        log.info("REST ip : {}", request.getRemoteAddr());
        log.info("REST node : {}", node);
                        
        
        MonitorNodeDTO dto = new MonitorNodeDTO(node.getId(), node.getName(), ZonedDateTime.now(), request.getRemoteAddr() );
        
        //Sent Menssage
        sentMessage (dto);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-remotnitoringApp-ping", createHeaderResultPing(dto, userLogin) );
        return ResponseEntity.ok().headers(headers).build();
	}    
    
    
    private void sentMessage (MonitorNodeDTO dto) {    	    	
    		outputEchoChannel.send(MessageBuilder.withPayload(dto)
        		   .build());        
    }
    

    private String createHeaderResultPing (MonitorNodeDTO dto, String userLogin) {
    		return MessageFormat.format("Ping done at {3} for Node[{0}:{1}] from IP:{2}", 
    				userLogin,
    				dto.getNodeName(), 
    				dto.getIp(), 
    				dto.getLastHeartbeat().toOffsetDateTime().toString() );
    }
    
    
}
