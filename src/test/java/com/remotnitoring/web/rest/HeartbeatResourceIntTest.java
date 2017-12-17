package com.remotnitoring.web.rest;

import com.remotnitoring.RemotnitoringApp;

import com.remotnitoring.domain.Heartbeat;
import com.remotnitoring.domain.Node;
import com.remotnitoring.repository.HeartbeatRepository;
import com.remotnitoring.repository.NodeRepository;
import com.remotnitoring.service.HeartbeatService;
import com.remotnitoring.web.rest.errors.ExceptionTranslator;
import com.remotnitoring.service.dto.HeartbeatCriteria;
import com.remotnitoring.service.HeartbeatQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.remotnitoring.web.rest.TestUtil.sameInstant;
import static com.remotnitoring.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HeartbeatResource REST controller.
 *
 * @see HeartbeatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemotnitoringApp.class)
public class HeartbeatResourceIntTest {

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    @Autowired
    private NodeRepository nodeRepository;
    
    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Autowired
    private HeartbeatService heartbeatService;

    @Autowired
    private HeartbeatQueryService heartbeatQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHeartbeatMockMvc;

    private Heartbeat heartbeat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HeartbeatResource heartbeatResource = new HeartbeatResource(heartbeatService, heartbeatQueryService, nodeRepository);
        this.restHeartbeatMockMvc = MockMvcBuilders.standaloneSetup(heartbeatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Heartbeat createEntity(EntityManager em) {
        Heartbeat heartbeat = new Heartbeat()
            .timestamp(DEFAULT_TIMESTAMP)
            .ip(DEFAULT_IP);
        // Add required entity
        Node node = NodeResourceIntTest.createEntity(em);
        em.persist(node);
        em.flush();
        heartbeat.setNode(node);
        return heartbeat;
    }

    @Before
    public void initTest() {
        heartbeat = createEntity(em);
    }

    @Test
    @Transactional
    public void createHeartbeat() throws Exception {
        int databaseSizeBeforeCreate = heartbeatRepository.findAll().size();

        // Create the Heartbeat
        restHeartbeatMockMvc.perform(post("/api/heartbeats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heartbeat)))
            .andExpect(status().isCreated());

        // Validate the Heartbeat in the database
        List<Heartbeat> heartbeatList = heartbeatRepository.findAll();
        assertThat(heartbeatList).hasSize(databaseSizeBeforeCreate + 1);
        Heartbeat testHeartbeat = heartbeatList.get(heartbeatList.size() - 1);
        assertThat(testHeartbeat.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testHeartbeat.getIp()).isEqualTo(DEFAULT_IP);
    }

    @Test
    @Transactional
    public void createHeartbeatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = heartbeatRepository.findAll().size();

        // Create the Heartbeat with an existing ID
        heartbeat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeartbeatMockMvc.perform(post("/api/heartbeats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heartbeat)))
            .andExpect(status().isBadRequest());

        // Validate the Heartbeat in the database
        List<Heartbeat> heartbeatList = heartbeatRepository.findAll();
        assertThat(heartbeatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = heartbeatRepository.findAll().size();
        // set the field null
        heartbeat.setTimestamp(null);

        // Create the Heartbeat, which fails.

        restHeartbeatMockMvc.perform(post("/api/heartbeats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heartbeat)))
            .andExpect(status().isBadRequest());

        List<Heartbeat> heartbeatList = heartbeatRepository.findAll();
        assertThat(heartbeatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHeartbeats() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList
        restHeartbeatMockMvc.perform(get("/api/heartbeats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(heartbeat.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())));
    }

    @Test
    @Transactional
    public void getHeartbeat() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get the heartbeat
        restHeartbeatMockMvc.perform(get("/api/heartbeats/{id}", heartbeat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(heartbeat.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP.toString()));
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where timestamp equals to DEFAULT_TIMESTAMP
        defaultHeartbeatShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the heartbeatList where timestamp equals to UPDATED_TIMESTAMP
        defaultHeartbeatShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultHeartbeatShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the heartbeatList where timestamp equals to UPDATED_TIMESTAMP
        defaultHeartbeatShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where timestamp is not null
        defaultHeartbeatShouldBeFound("timestamp.specified=true");

        // Get all the heartbeatList where timestamp is null
        defaultHeartbeatShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultHeartbeatShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the heartbeatList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultHeartbeatShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultHeartbeatShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the heartbeatList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultHeartbeatShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllHeartbeatsByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where ip equals to DEFAULT_IP
        defaultHeartbeatShouldBeFound("ip.equals=" + DEFAULT_IP);

        // Get all the heartbeatList where ip equals to UPDATED_IP
        defaultHeartbeatShouldNotBeFound("ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByIpIsInShouldWork() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where ip in DEFAULT_IP or UPDATED_IP
        defaultHeartbeatShouldBeFound("ip.in=" + DEFAULT_IP + "," + UPDATED_IP);

        // Get all the heartbeatList where ip equals to UPDATED_IP
        defaultHeartbeatShouldNotBeFound("ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        heartbeatRepository.saveAndFlush(heartbeat);

        // Get all the heartbeatList where ip is not null
        defaultHeartbeatShouldBeFound("ip.specified=true");

        // Get all the heartbeatList where ip is null
        defaultHeartbeatShouldNotBeFound("ip.specified=false");
    }

    @Test
    @Transactional
    public void getAllHeartbeatsByNodeIsEqualToSomething() throws Exception {
        // Initialize the database
        Node node = NodeResourceIntTest.createEntity(em);
        em.persist(node);
        em.flush();
        heartbeat.setNode(node);
        heartbeatRepository.saveAndFlush(heartbeat);
        Long nodeId = node.getId();

        // Get all the heartbeatList where node equals to nodeId
        defaultHeartbeatShouldBeFound("nodeId.equals=" + nodeId);

        // Get all the heartbeatList where node equals to nodeId + 1
        defaultHeartbeatShouldNotBeFound("nodeId.equals=" + (nodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHeartbeatShouldBeFound(String filter) throws Exception {
        restHeartbeatMockMvc.perform(get("/api/heartbeats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(heartbeat.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHeartbeatShouldNotBeFound(String filter) throws Exception {
        restHeartbeatMockMvc.perform(get("/api/heartbeats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingHeartbeat() throws Exception {
        // Get the heartbeat
        restHeartbeatMockMvc.perform(get("/api/heartbeats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHeartbeat() throws Exception {
        // Initialize the database
        heartbeatService.save(heartbeat);

        int databaseSizeBeforeUpdate = heartbeatRepository.findAll().size();

        // Update the heartbeat
        Heartbeat updatedHeartbeat = heartbeatRepository.findOne(heartbeat.getId());
        // Disconnect from session so that the updates on updatedHeartbeat are not directly saved in db
        em.detach(updatedHeartbeat);
        updatedHeartbeat
            .timestamp(UPDATED_TIMESTAMP)
            .ip(UPDATED_IP);

        restHeartbeatMockMvc.perform(put("/api/heartbeats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHeartbeat)))
            .andExpect(status().isOk());

        // Validate the Heartbeat in the database
        List<Heartbeat> heartbeatList = heartbeatRepository.findAll();
        assertThat(heartbeatList).hasSize(databaseSizeBeforeUpdate);
        Heartbeat testHeartbeat = heartbeatList.get(heartbeatList.size() - 1);
        assertThat(testHeartbeat.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testHeartbeat.getIp()).isEqualTo(UPDATED_IP);
    }

    @Test
    @Transactional
    public void updateNonExistingHeartbeat() throws Exception {
        int databaseSizeBeforeUpdate = heartbeatRepository.findAll().size();

        // Create the Heartbeat

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHeartbeatMockMvc.perform(put("/api/heartbeats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heartbeat)))
            .andExpect(status().isCreated());

        // Validate the Heartbeat in the database
        List<Heartbeat> heartbeatList = heartbeatRepository.findAll();
        assertThat(heartbeatList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHeartbeat() throws Exception {
        // Initialize the database
        heartbeatService.save(heartbeat);

        int databaseSizeBeforeDelete = heartbeatRepository.findAll().size();

        // Get the heartbeat
        restHeartbeatMockMvc.perform(delete("/api/heartbeats/{id}", heartbeat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Heartbeat> heartbeatList = heartbeatRepository.findAll();
        assertThat(heartbeatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Heartbeat.class);
        Heartbeat heartbeat1 = new Heartbeat();
        heartbeat1.setId(1L);
        Heartbeat heartbeat2 = new Heartbeat();
        heartbeat2.setId(heartbeat1.getId());
        assertThat(heartbeat1).isEqualTo(heartbeat2);
        heartbeat2.setId(2L);
        assertThat(heartbeat1).isNotEqualTo(heartbeat2);
        heartbeat1.setId(null);
        assertThat(heartbeat1).isNotEqualTo(heartbeat2);
    }
}
