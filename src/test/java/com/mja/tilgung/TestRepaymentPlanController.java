package com.mja.tilgung;

import com.mja.tilgung.controllers.RepaymentPlanController;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class TestRepaymentPlanController {
    private MockMvc mockMvc;
    private String requestBody;

    @InjectMocks
    private RepaymentPlanController repaymentPlanController;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(repaymentPlanController).build();
        requestBody = "{" +
                "\"startDate\": \"01.11.2015\"," +
                "\"loan\": 100000," +
                "\"interestRate\": 2.12," +
                "\"durationFixedInterest\": 10," +
                "\"initialRepayment\": 2" +
                "}";
    }

    @Test
    public void repaymentPlanList_WithSomeRequestData_ShouldReturnExpectedResults()  throws Exception{
        mockMvc.perform(post("/api/repayment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(121)))
                .andExpect(jsonPath("$[0].date", Matchers.is("30.11.2015")));
    }
}
