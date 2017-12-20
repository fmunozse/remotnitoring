package com.remotnitoring.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.domain.Node;
import com.remotnitoring.repository.NodeRepository;
import com.remotnitoring.security.SecurityUtils;
import com.remotnitoring.service.HeartbeatService;
import com.remotnitoring.web.rest.errors.BadRequestAlertException;
import com.remotnitoring.web.rest.errors.InternalServerErrorException;
import com.remotnitoring.web.rest.util.HeaderUtil;
import com.remotnitoring.web.rest.util.PaginationUtil;
import com.remotnitoring.service.dto.HeartbeatCriteria;
import com.remotnitoring.service.HeartbeatQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Heartbeat.
 */
@RestController
@RequestMapping("/api")
public class HeartbeatResource {

    private final Logger log = LoggerFactory.getLogger(HeartbeatResource.class);

    private static final String ENTITY_NAME = "heartbeat";

    private final HeartbeatService heartbeatService;
    
    private final HeartbeatQueryService heartbeatQueryService;
    
    public HeartbeatResource(HeartbeatService heartbeatService, HeartbeatQueryService heartbeatQueryService, NodeRepository nodeRepository) {
        this.heartbeatService = heartbeatService;
        this.heartbeatQueryService = heartbeatQueryService;
    }

    /**
     * POST  /heartbeats : Create a new heartbeat.
     *
     * @param heartbeat the heartbeat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new heartbeat, or with status 400 (Bad Request) if the heartbeat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/heartbeats")
    @Timed
    public ResponseEntity<Heartbeat> createHeartbeat(@Valid @RequestBody Heartbeat heartbeat) throws URISyntaxException {
        log.debug("REST request to save Heartbeat : {}", heartbeat);
        if (heartbeat.getId() != null) {
            throw new BadRequestAlertException("A new heartbeat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Heartbeat result = heartbeatService.save(heartbeat);
        return ResponseEntity.created(new URI("/api/heartbeats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /heartbeats : Updates an existing heartbeat.
     *
     * @param heartbeat the heartbeat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated heartbeat,
     * or with status 400 (Bad Request) if the heartbeat is not valid,
     * or with status 500 (Internal Server Error) if the heartbeat couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/heartbeats")
    @Timed
    public ResponseEntity<Heartbeat> updateHeartbeat(@Valid @RequestBody Heartbeat heartbeat) throws URISyntaxException {
        log.debug("REST request to update Heartbeat : {}", heartbeat);
        if (heartbeat.getId() == null) {
            return createHeartbeat(heartbeat);
        }
        Heartbeat result = heartbeatService.save(heartbeat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, heartbeat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /heartbeats : get all the heartbeats.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of heartbeats in body
     */
    @GetMapping("/heartbeats")
    @Timed
    public ResponseEntity<List<Heartbeat>> getAllHeartbeats(HeartbeatCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Heartbeats by criteria: {}", criteria);
        Page<Heartbeat> page = heartbeatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/heartbeats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /heartbeats/:id : get the "id" heartbeat.
     *
     * @param id the id of the heartbeat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the heartbeat, or with status 404 (Not Found)
     */
    @GetMapping("/heartbeats/{id}")
    @Timed
    public ResponseEntity<Heartbeat> getHeartbeat(@PathVariable Long id) {
        log.debug("REST request to get Heartbeat : {}", id);
        Heartbeat heartbeat = heartbeatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(heartbeat));
    }

    /**
     * DELETE  /heartbeats/:id : delete the "id" heartbeat.
     *
     * @param id the id of the heartbeat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/heartbeats/{id}")
    @Timed
    public ResponseEntity<Void> deleteHeartbeat(@PathVariable Long id) {
        log.debug("REST request to delete Heartbeat : {}", id);
        heartbeatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
