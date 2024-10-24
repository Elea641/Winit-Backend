package com.cda.winit;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String authToken;

    @BeforeEach
    public void setUp() throws Exception {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("email", "john@doe.com");
        jsonUser.put("password", "johndoe");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(jsonUser.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseContent);
        authToken = jsonResponse.getString("token");
    }

    @Test
    public void testGetAllGeneratedTournaments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tournaments/generated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllOpenTournaments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tournaments/open")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllTournamentsByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tournaments/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tournaments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetTournamentsByFilter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tournaments/filter")
                        .param("searchValue", "Tournament")
                        .param("selectedSport", "Volley")
                        .param("chronologicalFilter", "true")
                        .param("showOnlyUpcomingTournaments", "true")
                        .param("showNonFullTournaments", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testUpdateTournament() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("isGenerated", true);
        requestBody.put("isCanceled", false);
        requestBody.put("matches", null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tournaments/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testDeleteTournament() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tournaments/tournament3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testDeleteTournamentIsGeneratedBadResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tournaments/tournament2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Impossible de supprimer un tournoi confirmé")))
                .andReturn();
    }
}
