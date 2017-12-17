package com.remotnitoring.web.rest;

import com.remotnitoring.RemotnitoringApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the Dashboard REST controller.
 *
 * @see DashboardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemotnitoringApp.class)
public class DashboardResourceIntTest {

    private MockMvc restMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
/*
        DashboardResource dashboardResource = new DashboardResource();
        restMockMvc = MockMvcBuilders
            .standaloneSetup(dashboardResource)
            .build();
            
            */
            
    }

    /**
    * Test lastSituation
    */
    @Test
    public void testLastSituation() throws Exception {
    	/*
        restMockMvc.perform(get("/api/dashboard/last-situation"))
            .andExpect(status().isOk());
            */
    }

    
    
}
