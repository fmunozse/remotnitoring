package com.remotnitoring.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.remotnitoring.domain.RequestRemoteCommand;

import com.remotnitoring.repository.RequestRemoteCommandRepository;
import com.remotnitoring.web.rest.errors.BadRequestAlertException;
import com.remotnitoring.web.rest.util.HeaderUtil;
import com.remotnitoring.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RequestRemoteCommand.
 */
@RestController
@RequestMapping("/api")
public class RequestRemoteCommandResource {

    private final Logger log = LoggerFactory.getLogger(RequestRemoteCommandResource.class);

    private static final String ENTITY_NAME = "requestRemoteCommand";

    private final RequestRemoteCommandRepository requestRemoteCommandRepository;

    public RequestRemoteCommandResource(RequestRemoteCommandRepository requestRemoteCommandRepository) {
        this.requestRemoteCommandRepository = requestRemoteCommandRepository;
    }

    /**
     * POST  /request-remote-commands : Create a new requestRemoteCommand.
     *
     * @param requestRemoteCommand the requestRemoteCommand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requestRemoteCommand, or with status 400 (Bad Request) if the requestRemoteCommand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/request-remote-commands")
    @Timed
    public ResponseEntity<RequestRemoteCommand> createRequestRemoteCommand(@Valid @RequestBody RequestRemoteCommand requestRemoteCommand) throws URISyntaxException {
        log.debug("REST request to save RequestRemoteCommand : {}", requestRemoteCommand);
        if (requestRemoteCommand.getId() != null) {
            throw new BadRequestAlertException("A new requestRemoteCommand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequestRemoteCommand result = requestRemoteCommandRepository.save(requestRemoteCommand);
        return ResponseEntity.created(new URI("/api/request-remote-commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /request-remote-commands : Updates an existing requestRemoteCommand.
     *
     * @param requestRemoteCommand the requestRemoteCommand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requestRemoteCommand,
     * or with status 400 (Bad Request) if the requestRemoteCommand is not valid,
     * or with status 500 (Internal Server Error) if the requestRemoteCommand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/request-remote-commands")
    @Timed
    public ResponseEntity<RequestRemoteCommand> updateRequestRemoteCommand(@Valid @RequestBody RequestRemoteCommand requestRemoteCommand) throws URISyntaxException {
        log.debug("REST request to update RequestRemoteCommand : {}", requestRemoteCommand);
        if (requestRemoteCommand.getId() == null) {
            return createRequestRemoteCommand(requestRemoteCommand);
        }
        RequestRemoteCommand result = requestRemoteCommandRepository.save(requestRemoteCommand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, requestRemoteCommand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /request-remote-commands : get all the requestRemoteCommands.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of requestRemoteCommands in body
     */
    @GetMapping("/request-remote-commands")
    @Timed
    public ResponseEntity<List<RequestRemoteCommand>> getAllRequestRemoteCommands(Pageable pageable) {
        log.debug("REST request to get a page of RequestRemoteCommands");
        Page<RequestRemoteCommand> page = requestRemoteCommandRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/request-remote-commands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /request-remote-commands/:id : get the "id" requestRemoteCommand.
     *
     * @param id the id of the requestRemoteCommand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requestRemoteCommand, or with status 404 (Not Found)
     */
    @GetMapping("/request-remote-commands/{id}")
    @Timed
    public ResponseEntity<RequestRemoteCommand> getRequestRemoteCommand(@PathVariable Long id) {
        log.debug("REST request to get RequestRemoteCommand : {}", id);
        RequestRemoteCommand requestRemoteCommand = requestRemoteCommandRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(requestRemoteCommand));
    }

    /**
     * DELETE  /request-remote-commands/:id : delete the "id" requestRemoteCommand.
     *
     * @param id the id of the requestRemoteCommand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/request-remote-commands/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequestRemoteCommand(@PathVariable Long id) {
        log.debug("REST request to delete RequestRemoteCommand : {}", id);
        requestRemoteCommandRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
