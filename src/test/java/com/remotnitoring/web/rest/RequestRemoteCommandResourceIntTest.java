package com.remotnitoring.web.rest;

import com.remotnitoring.RemotnitoringApp;

import com.remotnitoring.domain.RequestRemoteCommand;
import com.remotnitoring.domain.Node;
import com.remotnitoring.repository.RequestRemoteCommandRepository;
import com.remotnitoring.web.rest.errors.ExceptionTranslator;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.remotnitoring.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.remotnitoring.domain.enumeration.StatusRemoteCommand;
/**
 * Test class for the RequestRemoteCommandResource REST controller.
 *
 * @see RequestRemoteCommandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemotnitoringApp.class)
public class RequestRemoteCommandResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMAND = "AAAAAAAAAA";
    private static final String UPDATED_COMMAND = "BBBBBBBBBB";

    private static final StatusRemoteCommand DEFAULT_STATUS = StatusRemoteCommand.Pending;
    private static final StatusRemoteCommand UPDATED_STATUS = StatusRemoteCommand.Completed;

    @Autowired
    private RequestRemoteCommandRepository requestRemoteCommandRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRequestRemoteCommandMockMvc;

    private RequestRemoteCommand requestRemoteCommand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequestRemoteCommandResource requestRemoteCommandResource = new RequestRemoteCommandResource(requestRemoteCommandRepository);
        this.restRequestRemoteCommandMockMvc = MockMvcBuilders.standaloneSetup(requestRemoteCommandResource)
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
    public static RequestRemoteCommand createEntity(EntityManager em) {
        RequestRemoteCommand requestRemoteCommand = new RequestRemoteCommand()
            .description(DEFAULT_DESCRIPTION)
            .command(DEFAULT_COMMAND)
            .status(DEFAULT_STATUS);
        // Add required entity
        Node node = NodeResourceIntTest.createEntity(em);
        em.persist(node);
        em.flush();
        requestRemoteCommand.setNode(node);
        return requestRemoteCommand;
    }

    @Before
    public void initTest() {
        requestRemoteCommand = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequestRemoteCommand() throws Exception {
        int databaseSizeBeforeCreate = requestRemoteCommandRepository.findAll().size();

        // Create the RequestRemoteCommand
        restRequestRemoteCommandMockMvc.perform(post("/api/request-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestRemoteCommand)))
            .andExpect(status().isCreated());

        // Validate the RequestRemoteCommand in the database
        List<RequestRemoteCommand> requestRemoteCommandList = requestRemoteCommandRepository.findAll();
        assertThat(requestRemoteCommandList).hasSize(databaseSizeBeforeCreate + 1);
        RequestRemoteCommand testRequestRemoteCommand = requestRemoteCommandList.get(requestRemoteCommandList.size() - 1);
        assertThat(testRequestRemoteCommand.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequestRemoteCommand.getCommand()).isEqualTo(DEFAULT_COMMAND);
        assertThat(testRequestRemoteCommand.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRequestRemoteCommandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requestRemoteCommandRepository.findAll().size();

        // Create the RequestRemoteCommand with an existing ID
        requestRemoteCommand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestRemoteCommandMockMvc.perform(post("/api/request-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestRemoteCommand)))
            .andExpect(status().isBadRequest());

        // Validate the RequestRemoteCommand in the database
        List<RequestRemoteCommand> requestRemoteCommandList = requestRemoteCommandRepository.findAll();
        assertThat(requestRemoteCommandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRemoteCommandRepository.findAll().size();
        // set the field null
        requestRemoteCommand.setDescription(null);

        // Create the RequestRemoteCommand, which fails.

        restRequestRemoteCommandMockMvc.perform(post("/api/request-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestRemoteCommand)))
            .andExpect(status().isBadRequest());

        List<RequestRemoteCommand> requestRemoteCommandList = requestRemoteCommandRepository.findAll();
        assertThat(requestRemoteCommandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequestRemoteCommands() throws Exception {
        // Initialize the database
        requestRemoteCommandRepository.saveAndFlush(requestRemoteCommand);

        // Get all the requestRemoteCommandList
        restRequestRemoteCommandMockMvc.perform(get("/api/request-remote-commands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestRemoteCommand.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].command").value(hasItem(DEFAULT_COMMAND.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getRequestRemoteCommand() throws Exception {
        // Initialize the database
        requestRemoteCommandRepository.saveAndFlush(requestRemoteCommand);

        // Get the requestRemoteCommand
        restRequestRemoteCommandMockMvc.perform(get("/api/request-remote-commands/{id}", requestRemoteCommand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requestRemoteCommand.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.command").value(DEFAULT_COMMAND.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequestRemoteCommand() throws Exception {
        // Get the requestRemoteCommand
        restRequestRemoteCommandMockMvc.perform(get("/api/request-remote-commands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequestRemoteCommand() throws Exception {
        // Initialize the database
        requestRemoteCommandRepository.saveAndFlush(requestRemoteCommand);
        int databaseSizeBeforeUpdate = requestRemoteCommandRepository.findAll().size();

        // Update the requestRemoteCommand
        RequestRemoteCommand updatedRequestRemoteCommand = requestRemoteCommandRepository.findOne(requestRemoteCommand.getId());
        // Disconnect from session so that the updates on updatedRequestRemoteCommand are not directly saved in db
        em.detach(updatedRequestRemoteCommand);
        updatedRequestRemoteCommand
            .description(UPDATED_DESCRIPTION)
            .command(UPDATED_COMMAND)
            .status(UPDATED_STATUS);

        restRequestRemoteCommandMockMvc.perform(put("/api/request-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequestRemoteCommand)))
            .andExpect(status().isOk());

        // Validate the RequestRemoteCommand in the database
        List<RequestRemoteCommand> requestRemoteCommandList = requestRemoteCommandRepository.findAll();
        assertThat(requestRemoteCommandList).hasSize(databaseSizeBeforeUpdate);
        RequestRemoteCommand testRequestRemoteCommand = requestRemoteCommandList.get(requestRemoteCommandList.size() - 1);
        assertThat(testRequestRemoteCommand.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequestRemoteCommand.getCommand()).isEqualTo(UPDATED_COMMAND);
        assertThat(testRequestRemoteCommand.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRequestRemoteCommand() throws Exception {
        int databaseSizeBeforeUpdate = requestRemoteCommandRepository.findAll().size();

        // Create the RequestRemoteCommand

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRequestRemoteCommandMockMvc.perform(put("/api/request-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestRemoteCommand)))
            .andExpect(status().isCreated());

        // Validate the RequestRemoteCommand in the database
        List<RequestRemoteCommand> requestRemoteCommandList = requestRemoteCommandRepository.findAll();
        assertThat(requestRemoteCommandList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRequestRemoteCommand() throws Exception {
        // Initialize the database
        requestRemoteCommandRepository.saveAndFlush(requestRemoteCommand);
        int databaseSizeBeforeDelete = requestRemoteCommandRepository.findAll().size();

        // Get the requestRemoteCommand
        restRequestRemoteCommandMockMvc.perform(delete("/api/request-remote-commands/{id}", requestRemoteCommand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RequestRemoteCommand> requestRemoteCommandList = requestRemoteCommandRepository.findAll();
        assertThat(requestRemoteCommandList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestRemoteCommand.class);
        RequestRemoteCommand requestRemoteCommand1 = new RequestRemoteCommand();
        requestRemoteCommand1.setId(1L);
        RequestRemoteCommand requestRemoteCommand2 = new RequestRemoteCommand();
        requestRemoteCommand2.setId(requestRemoteCommand1.getId());
        assertThat(requestRemoteCommand1).isEqualTo(requestRemoteCommand2);
        requestRemoteCommand2.setId(2L);
        assertThat(requestRemoteCommand1).isNotEqualTo(requestRemoteCommand2);
        requestRemoteCommand1.setId(null);
        assertThat(requestRemoteCommand1).isNotEqualTo(requestRemoteCommand2);
    }
}
