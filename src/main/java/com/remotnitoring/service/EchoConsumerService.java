package com.remotnitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.domain.Node;
import com.remotnitoring.messaging.Channels;
import com.remotnitoring.messaging.example.ConsumerChannel;
import com.remotnitoring.messaging.example.ConsumerService;
import com.remotnitoring.messaging.example.Greeting;
import com.remotnitoring.repository.HeartbeatRepository;
import com.remotnitoring.repository.NodeRepository;
import com.remotnitoring.service.dto.MonitorNodeDTO;

@Service
public class EchoConsumerService {

    private final Logger log = LoggerFactory.getLogger(EchoConsumerService.class);

    private SimpMessagingTemplate messagingTemplate;
    
    private HeartbeatService heartbeatService;
    
    private NodeRepository nodeRepository;
    
    public EchoConsumerService (SimpMessagingTemplate messagingTemplate, HeartbeatService heartbeatService, NodeRepository nodeRepository) {
    		this.messagingTemplate = messagingTemplate;
    		this.heartbeatService = heartbeatService;
    		this.nodeRepository = nodeRepository;
    		log.info("Constructor: EchoConsumerService {}", this.toString());
    }
    
    @StreamListener(Channels.INPUT_ECHO_CHANNEL)
    //@SendTo("/topic/echo")
    public void consume(Message<MonitorNodeDTO> message) {    	
        log.info("Received message.headers: {}.", message.getHeaders().toString());
        
        MonitorNodeDTO monitorNodeDTO = message.getPayload();
        log.info("Received message: {}.", monitorNodeDTO.toString());
        
        //Find node
        Node node = nodeRepository.findOne(monitorNodeDTO.getNodeId());
        
        //Create heatbeat and save it
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setIp(monitorNodeDTO.getIp());
        heartbeat.setNode(node);
        heartbeat.setTimestamp(monitorNodeDTO.getLastHeartbeat());
        heartbeatService.save(heartbeat);
                
        //Send to websocket        
        messagingTemplate.convertAndSend("/topic/echo", monitorNodeDTO);

    }    
}
