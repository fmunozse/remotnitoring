package com.remotnitoring.web.rest;

import com.remotnitoring.RemotnitoringApp;

import com.remotnitoring.domain.ResponseRemoteCommand;
import com.remotnitoring.domain.RequestRemoteCommand;
import com.remotnitoring.repository.ResponseRemoteCommandRepository;
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
 * Test class for the ResponseRemoteCommandResource REST controller.
 *
 * @see ResponseRemoteCommandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemotnitoringApp.class)
public class ResponseRemoteCommandResourceIntTest {

    private static final ZonedDateTime DEFAULT_WHEN_EXECUTED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_WHEN_EXECUTED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LOG_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_LOG_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_COD_RETURN = "AAAAAAAAAA";
    private static final String UPDATED_COD_RETURN = "BBBBBBBBBB";

    @Autowired
    private ResponseRemoteCommandRepository responseRemoteCommandRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResponseRemoteCommandMockMvc;

    private ResponseRemoteCommand responseRemoteCommand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResponseRemoteCommandResource responseRemoteCommandResource = new ResponseRemoteCommandResource(responseRemoteCommandRepository);
        this.restResponseRemoteCommandMockMvc = MockMvcBuilders.standaloneSetup(responseRemoteCommandResource)
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
    public static ResponseRemoteCommand createEntity(EntityManager em) {
        ResponseRemoteCommand responseRemoteCommand = new ResponseRemoteCommand()
            .whenExecuted(DEFAULT_WHEN_EXECUTED)
            .logResult(DEFAULT_LOG_RESULT)
            .codReturn(DEFAULT_COD_RETURN);
        // Add required entity
        RequestRemoteCommand requestRemoteCommand = RequestRemoteCommandResourceIntTest.createEntity(em);
        em.persist(requestRemoteCommand);
        em.flush();
        responseRemoteCommand.setRequestRemoteCommand(requestRemoteCommand);
        return responseRemoteCommand;
    }

    @Before
    public void initTest() {
        responseRemoteCommand = createEntity(em);
    }

    @Test
    @Transactional
    public void createResponseRemoteCommand() throws Exception {
        int databaseSizeBeforeCreate = responseRemoteCommandRepository.findAll().size();

        // Create the ResponseRemoteCommand
        restResponseRemoteCommandMockMvc.perform(post("/api/response-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responseRemoteCommand)))
            .andExpect(status().isCreated());

        // Validate the ResponseRemoteCommand in the database
        List<ResponseRemoteCommand> responseRemoteCommandList = responseRemoteCommandRepository.findAll();
        assertThat(responseRemoteCommandList).hasSize(databaseSizeBeforeCreate + 1);
        ResponseRemoteCommand testResponseRemoteCommand = responseRemoteCommandList.get(responseRemoteCommandList.size() - 1);
        assertThat(testResponseRemoteCommand.getWhenExecuted()).isEqualTo(DEFAULT_WHEN_EXECUTED);
        assertThat(testResponseRemoteCommand.getLogResult()).isEqualTo(DEFAULT_LOG_RESULT);
        assertThat(testResponseRemoteCommand.getCodReturn()).isEqualTo(DEFAULT_COD_RETURN);
    }

    @Test
    @Transactional
    public void createResponseRemoteCommandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = responseRemoteCommandRepository.findAll().size();

        // Create the ResponseRemoteCommand with an existing ID
        responseRemoteCommand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponseRemoteCommandMockMvc.perform(post("/api/response-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responseRemoteCommand)))
            .andExpect(status().isBadRequest());

        // Validate the ResponseRemoteCommand in the database
        List<ResponseRemoteCommand> responseRemoteCommandList = responseRemoteCommandRepository.findAll();
        assertThat(responseRemoteCommandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllResponseRemoteCommands() throws Exception {
        // Initialize the database
        responseRemoteCommandRepository.saveAndFlush(responseRemoteCommand);

        // Get all the responseRemoteCommandList
        restResponseRemoteCommandMockMvc.perform(get("/api/response-remote-commands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responseRemoteCommand.getId().intValue())))
            .andExpect(jsonPath("$.[*].whenExecuted").value(hasItem(sameInstant(DEFAULT_WHEN_EXECUTED))))
            .andExpect(jsonPath("$.[*].logResult").value(hasItem(DEFAULT_LOG_RESULT.toString())))
            .andExpect(jsonPath("$.[*].codReturn").value(hasItem(DEFAULT_COD_RETURN.toString())));
    }

    @Test
    @Transactional
    public void getResponseRemoteCommand() throws Exception {
        // Initialize the database
        responseRemoteCommandRepository.saveAndFlush(responseRemoteCommand);

        // Get the responseRemoteCommand
        restResponseRemoteCommandMockMvc.perform(get("/api/response-remote-commands/{id}", responseRemoteCommand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(responseRemoteCommand.getId().intValue()))
            .andExpect(jsonPath("$.whenExecuted").value(sameInstant(DEFAULT_WHEN_EXECUTED)))
            .andExpect(jsonPath("$.logResult").value(DEFAULT_LOG_RESULT.toString()))
            .andExpect(jsonPath("$.codReturn").value(DEFAULT_COD_RETURN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResponseRemoteCommand() throws Exception {
        // Get the responseRemoteCommand
        restResponseRemoteCommandMockMvc.perform(get("/api/response-remote-commands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResponseRemoteCommand() throws Exception {
        // Initialize the database
        responseRemoteCommandRepository.saveAndFlush(responseRemoteCommand);
        int databaseSizeBeforeUpdate = responseRemoteCommandRepository.findAll().size();

        // Update the responseRemoteCommand
        ResponseRemoteCommand updatedResponseRemoteCommand = responseRemoteCommandRepository.findOne(responseRemoteCommand.getId());
        // Disconnect from session so that the updates on updatedResponseRemoteCommand are not directly saved in db
        em.detach(updatedResponseRemoteCommand);
        updatedResponseRemoteCommand
            .whenExecuted(UPDATED_WHEN_EXECUTED)
            .logResult(UPDATED_LOG_RESULT)
            .codReturn(UPDATED_COD_RETURN);

        restResponseRemoteCommandMockMvc.perform(put("/api/response-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResponseRemoteCommand)))
            .andExpect(status().isOk());

        // Validate the ResponseRemoteCommand in the database
        List<ResponseRemoteCommand> responseRemoteCommandList = responseRemoteCommandRepository.findAll();
        assertThat(responseRemoteCommandList).hasSize(databaseSizeBeforeUpdate);
        ResponseRemoteCommand testResponseRemoteCommand = responseRemoteCommandList.get(responseRemoteCommandList.size() - 1);
        assertThat(testResponseRemoteCommand.getWhenExecuted()).isEqualTo(UPDATED_WHEN_EXECUTED);
        assertThat(testResponseRemoteCommand.getLogResult()).isEqualTo(UPDATED_LOG_RESULT);
        assertThat(testResponseRemoteCommand.getCodReturn()).isEqualTo(UPDATED_COD_RETURN);
    }

    @Test
    @Transactional
    public void updateNonExistingResponseRemoteCommand() throws Exception {
        int databaseSizeBeforeUpdate = responseRemoteCommandRepository.findAll().size();

        // Create the ResponseRemoteCommand

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResponseRemoteCommandMockMvc.perform(put("/api/response-remote-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responseRemoteCommand)))
            .andExpect(status().isCreated());

        // Validate the ResponseRemoteCommand in the database
        List<ResponseRemoteCommand> responseRemoteCommandList = responseRemoteCommandRepository.findAll();
        assertThat(responseRemoteCommandList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResponseRemoteCommand() throws Exception {
        // Initialize the database
        responseRemoteCommandRepository.saveAndFlush(responseRemoteCommand);
        int databaseSizeBeforeDelete = responseRemoteCommandRepository.findAll().size();

        // Get the responseRemoteCommand
        restResponseRemoteCommandMockMvc.perform(delete("/api/response-remote-commands/{id}", responseRemoteCommand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ResponseRemoteCommand> responseRemoteCommandList = responseRemoteCommandRepository.findAll();
        assertThat(responseRemoteCommandList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponseRemoteCommand.class);
        ResponseRemoteCommand responseRemoteCommand1 = new ResponseRemoteCommand();
        responseRemoteCommand1.setId(1L);
        ResponseRemoteCommand responseRemoteCommand2 = new ResponseRemoteCommand();
        responseRemoteCommand2.setId(responseRemoteCommand1.getId());
        assertThat(responseRemoteCommand1).isEqualTo(responseRemoteCommand2);
        responseRemoteCommand2.setId(2L);
        assertThat(responseRemoteCommand1).isNotEqualTo(responseRemoteCommand2);
        responseRemoteCommand1.setId(null);
        assertThat(responseRemoteCommand1).isNotEqualTo(responseRemoteCommand2);
    }
}
