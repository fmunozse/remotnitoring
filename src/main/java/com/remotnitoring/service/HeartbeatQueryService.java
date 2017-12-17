package com.remotnitoring.service;


import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.domain.*; // for static metamodels
import com.remotnitoring.repository.HeartbeatRepository;
import com.remotnitoring.service.dto.HeartbeatCriteria;


/**
 * Service for executing complex queries for Heartbeat entities in the database.
 * The main input is a {@link HeartbeatCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Heartbeat} or a {@link Page} of {@link Heartbeat} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HeartbeatQueryService extends QueryService<Heartbeat> {

    private final Logger log = LoggerFactory.getLogger(HeartbeatQueryService.class);


    private final HeartbeatRepository heartbeatRepository;

    public HeartbeatQueryService(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    /**
     * Return a {@link List} of {@link Heartbeat} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Heartbeat> findByCriteria(HeartbeatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Heartbeat> specification = createSpecification(criteria);
        return heartbeatRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Heartbeat} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Heartbeat> findByCriteria(HeartbeatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Heartbeat> specification = createSpecification(criteria);
        return heartbeatRepository.findAll(specification, page);
    }

    /**
     * Function to convert HeartbeatCriteria to a {@link Specifications}
     */
    private Specifications<Heartbeat> createSpecification(HeartbeatCriteria criteria) {
        Specifications<Heartbeat> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Heartbeat_.id));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), Heartbeat_.timestamp));
            }
            if (criteria.getIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIp(), Heartbeat_.ip));
            }
            if (criteria.getNodeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getNodeId(), Heartbeat_.node, Node_.id));
            }
        }
        return specification;
    }

}
