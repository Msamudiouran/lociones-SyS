package com.locionesaaa.natillera;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NatilleraIntegrationTest {
    @Autowired WebApplicationContext context;
    @Autowired ObjectMapper mapper;
    MockMvc mvc;

    @BeforeAll void setUp(){ mvc=MockMvcBuilders.webAppContextSetup(context).build(); }

    @Test void managesCompleteNatilleraCycle() throws Exception {
        long cycle=id(post("/api/natillera/cycles", """
            {"name":"Navidad 2026","openingDate":"2026-01-01","fixedContribution":50000}
        """));
        long member=id(post("/api/natillera/members", """
            {"fullName":"Ana Pérez","document":"123456","phone":"3001234567"}
        """));
        long membership=id(post("/api/natillera/cycles/%d/members".formatted(cycle), """
            {"memberId":%d}
        """.formatted(member)));

        post("/api/natillera/cycles/%d/memberships/%d/contributions".formatted(cycle,membership), """
            {"amount":50000,"paidOn":"2026-02-01"}
        """).andExpect(status().isCreated());
        long loan=id(post("/api/natillera/cycles/%d/memberships/%d/loans".formatted(cycle,membership), """
            {"principal":100000,"annualInterestPercent":10}
        """));
        patch("/api/natillera/cycles/%d/loans/%d".formatted(cycle,loan), """{"status":"APPROVED"}""")
            .andExpect(status().isOk());
        post("/api/natillera/cycles/%d/loans/%d/payments".formatted(cycle,loan), """
            {"amount":110000,"paidOn":"2026-03-01"}
        """).andExpect(status().isCreated());

        MvcResult result=mvc.perform(get("/api/natillera/cycles/%d/memberships/%d/account".formatted(cycle,membership)))
            .andExpect(status().isOk()).andReturn();
        JsonNode account=mapper.readTree(result.getResponse().getContentAsString());
        assertThat(account.get("contributions").decimalValue()).isEqualByComparingTo("50000");
        assertThat(account.get("pendingLoanBalance").decimalValue()).isZero();

        post("/api/natillera/cycles/%d/close".formatted(cycle), "")
            .andExpect(status().isOk()).andExpect(jsonPath("$.totalSavings").value(50000));
        mvc.perform(get("/api/natillera/cycles/%d/memberships/%d/account/text".formatted(cycle,membership)))
            .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Estado de cuenta")));
    }

    private org.springframework.test.web.servlet.ResultActions post(String url,String body) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(body));
    }
    private org.springframework.test.web.servlet.ResultActions patch(String url,String body) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.patch(url).contentType(MediaType.APPLICATION_JSON).content(body));
    }
    private long id(org.springframework.test.web.servlet.ResultActions action) throws Exception {
        action.andExpect(status().isCreated());
        return mapper.readTree(action.andReturn().getResponse().getContentAsString()).get("id").asLong();
    }
}
