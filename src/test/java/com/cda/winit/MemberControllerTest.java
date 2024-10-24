package com.cda.winit;

import com.cda.winit.member.domain.dto.MemberRequest;
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
public class MemberControllerTest {

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
    public void testCreateMember() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "john@doe.com");

        MemberRequest memberRequest = new MemberRequest();

        memberRequest.setEmail(requestBody.getString("email"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/TeamA")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testDeleteMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/members/TeamA/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateMemberWithEmptyEmail() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/TeamA")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Erreur : Aucun utilisateur trouvé avec cet identifiant")))
                .andReturn();
    }

    @Test
    public void testDeleteMemberWithNonExistingTeamBadRequestResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/members/Team2/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Erreur : Aucune équipe trouvée avec ce nom: Team2")))
                .andReturn();
    }
}
