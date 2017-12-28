package com.remotnitoring.service;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.repository.HeartbeatRepository;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Heartbeat.
 */
@Service
@Transactional
public class HeartbeatService {

    private final Logger log = LoggerFactory.getLogger(HeartbeatService.class);

    private final HeartbeatRepository heartbeatRepository;

    public HeartbeatService(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }
    
    public void purgeOldHeartbeats () {
    		//heartbeatRepository.deleteByTimestampLessThan (ZonedDateTime.now().minusDays(7));    	
    }

    /**
     * Save a heartbeat.
     *
     * @param heartbeat the entity to save
     * @return the persisted entity
     */
    public Heartbeat save(Heartbeat heartbeat) {
        log.debug("Request to save Heartbeat : {}", heartbeat);
        return heartbeatRepository.save(heartbeat);
    }

    /**
     * Get all the heartbeats.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Heartbeat> findAll(Pageable pageable) {
        log.debug("Request to get all Heartbeats");
        return heartbeatRepository.findAll(pageable);
    }

    /**
     * Get one heartbeat by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Heartbeat findOne(Long id) {
        log.debug("Request to get Heartbeat : {}", id);
        return heartbeatRepository.findOne(id);
    }

    /**
     * Delete the heartbeat by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Heartbeat : {}", id);
        heartbeatRepository.delete(id);
    }
}
