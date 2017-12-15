package com.remotnitoring.messaging;

import static com.remotnitoring.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.remotnitoring.web.websocket.dto.ActivityDTO;

import net.bytebuddy.asm.Advice.This;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Service
public class ConsumerService {

    private final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    
    public ConsumerService() {
    	log.info("Constructor: ConsumerService {}", this.toString());
    }

    /*
    @SendTo("/topic/tracker")
    public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        activityDTO.setUserLogin(principal.getName());
        activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
        activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        activityDTO.setTime(Instant.now());
        log.debug("Sending user tracking data {}", activityDTO);
        return activityDTO;
    }
    */
    
    @StreamListener(ConsumerChannel.CHANNEL)
    //@SendTo("/topic/echo")
    public void consume(Message<Greeting> message) {
    	
    	
        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        
        log.info("Received message.headers: {}.", message.getHeaders().toString());
        log.info("Received acknowledgment: {}.", acknowledgment);
        
        Greeting greeting = message.getPayload();
        
        
        log.info("Received message: {}.", greeting.getMessage());
        messagingTemplate.convertAndSend("/topic/echo", greeting);

    }
}