package com.remotnitoring.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.remotnitoring.domain.ResponseRemoteCommand;

import com.remotnitoring.repository.ResponseRemoteCommandRepository;
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
 * REST controller for managing ResponseRemoteCommand.
 */
@RestController
@RequestMapping("/api")
public class ResponseRemoteCommandResource {

    private final Logger log = LoggerFactory.getLogger(ResponseRemoteCommandResource.class);

    private static final String ENTITY_NAME = "responseRemoteCommand";

    private final ResponseRemoteCommandRepository responseRemoteCommandRepository;

    public ResponseRemoteCommandResource(ResponseRemoteCommandRepository responseRemoteCommandRepository) {
        this.responseRemoteCommandRepository = responseRemoteCommandRepository;
    }

    /**
     * POST  /response-remote-commands : Create a new responseRemoteCommand.
     *
     * @param responseRemoteCommand the responseRemoteCommand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new responseRemoteCommand, or with status 400 (Bad Request) if the responseRemoteCommand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/response-remote-commands")
    @Timed
    public ResponseEntity<ResponseRemoteCommand> createResponseRemoteCommand(@Valid @RequestBody ResponseRemoteCommand responseRemoteCommand) throws URISyntaxException {
        log.debug("REST request to save ResponseRemoteCommand : {}", responseRemoteCommand);
        if (responseRemoteCommand.getId() != null) {
            throw new BadRequestAlertException("A new responseRemoteCommand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResponseRemoteCommand result = responseRemoteCommandRepository.save(responseRemoteCommand);
        return ResponseEntity.created(new URI("/api/response-remote-commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /response-remote-commands : Updates an existing responseRemoteCommand.
     *
     * @param responseRemoteCommand the responseRemoteCommand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated responseRemoteCommand,
     * or with status 400 (Bad Request) if the responseRemoteCommand is not valid,
     * or with status 500 (Internal Server Error) if the responseRemoteCommand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/response-remote-commands")
    @Timed
    public ResponseEntity<ResponseRemoteCommand> updateResponseRemoteCommand(@Valid @RequestBody ResponseRemoteCommand responseRemoteCommand) throws URISyntaxException {
        log.debug("REST request to update ResponseRemoteCommand : {}", responseRemoteCommand);
        if (responseRemoteCommand.getId() == null) {
            return createResponseRemoteCommand(responseRemoteCommand);
        }
        ResponseRemoteCommand result = responseRemoteCommandRepository.save(responseRemoteCommand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, responseRemoteCommand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /response-remote-commands : get all the responseRemoteCommands.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of responseRemoteCommands in body
     */
    @GetMapping("/response-remote-commands")
    @Timed
    public ResponseEntity<List<ResponseRemoteCommand>> getAllResponseRemoteCommands(Pageable pageable) {
        log.debug("REST request to get a page of ResponseRemoteCommands");
        Page<ResponseRemoteCommand> page = responseRemoteCommandRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/response-remote-commands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /response-remote-commands/:id : get the "id" responseRemoteCommand.
     *
     * @param id the id of the responseRemoteCommand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the responseRemoteCommand, or with status 404 (Not Found)
     */
    @GetMapping("/response-remote-commands/{id}")
    @Timed
    public ResponseEntity<ResponseRemoteCommand> getResponseRemoteCommand(@PathVariable Long id) {
        log.debug("REST request to get ResponseRemoteCommand : {}", id);
        ResponseRemoteCommand responseRemoteCommand = responseRemoteCommandRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(responseRemoteCommand));
    }

    /**
     * DELETE  /response-remote-commands/:id : delete the "id" responseRemoteCommand.
     *
     * @param id the id of the responseRemoteCommand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/response-remote-commands/{id}")
    @Timed
    public ResponseEntity<Void> deleteResponseRemoteCommand(@PathVariable Long id) {
        log.debug("REST request to delete ResponseRemoteCommand : {}", id);
        responseRemoteCommandRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
