package com.remotnitoring.web.rest;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.List;

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
    
    private HeartbeatService heartbeatService;
    
    private HeartbeatRepository heartbeatRepository;
        
    private NodeRepository nodeRepository;
        
    private MessageChannel outputEchoChannel;
    
    
    public MonitorResource(HeartbeatService heartbeatService, HeartbeatRepository heartbeatRepository, Channels channels, 
    		NodeRepository nodeRepository) {
		
    		super();
		this.heartbeatService = heartbeatService;
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

    		List<MonitorNodeDTO> lMonitorNodeDTO =  heartbeatRepository.countAllHeartBeatsPerNode();
    		
    		
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-MonitorNodeDTO-Total-Count", Long.toString(lMonitorNodeDTO.size()));        
        return new ResponseEntity<>(lMonitorNodeDTO, headers, HttpStatus.OK);

    }


    @PostMapping("/heartbeats/ping")
    @Timed
	public ResponseEntity<Void> ping(HttpServletRequest request) {
        log.debug("REST request to ping Heartbeat : {}", request);
        
        //Node node = find();
        
	    final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> 
	    		new InternalServerErrorException("Current user login not found"));        
	    final Node node = nodeRepository.findByUserIsCurrentUser().orElseThrow(() -> 
	    		new InternalServerErrorException("Current user login [" + userLogin + "] not found") );

        log.info("REST ip : {}", request.getRemoteAddr());
        log.info("REST node : {}", node);
        
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setIp(request.getRemoteAddr());
        heartbeat.setNode(node);
        heartbeat.setTimestamp(ZonedDateTime.now());
        Heartbeat result = heartbeatService.save(heartbeat);
                
        
        //Sent Menssage
        sentMessage (node, heartbeat);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-remotnitoringApp-ping", createHeaderResultPing(result) );
        return ResponseEntity.ok().headers(headers).build();
	}    
    
    
    private void sentMessage (Node node, Heartbeat heartbeat) {    	
    		MonitorNodeDTO dto = new MonitorNodeDTO(node.getId(), node.getName(), 1L, heartbeat.getTimestamp());    	
    		outputEchoChannel.send(MessageBuilder.withPayload(dto)
        		   .build());        
    }
    

    private String createHeaderResultPing (Heartbeat heartbeat) {
    		return MessageFormat.format("Created with id {0} of {1}, node {2} from ip {3} at {4} ", 
    				heartbeat.getId(), 
    				heartbeat.getNode().getUser().getLogin(),
    				heartbeat.getNode().getName(), 
    				heartbeat.getIp(), 
    				heartbeat.getTimestamp().toOffsetDateTime().toString() );
    }
    
    
}
