package com.cda.winit;

import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.sport.domain.service.SportService;
import com.cda.winit.team.domain.dto.TeamDto;
import com.cda.winit.team.domain.dto.TeamUpdateDto;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.infrastructure.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
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

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SportService sportService;

    private String authToken;
    private User user;
    private Sport sport;

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

        user = userRepository.findByEmail("john@doe.com").get();

        sport = new Sport(1L, "Volley", "image", 6, new ArrayList<>());
        sportService.saveSport(sport);
    }

    @Test
    public void testGetAllTeamsOfUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetTeamsBySport() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/sport/Volley")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateTeam() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 2);
        requestBody.put("name", "New Team");
        requestBody.put("sport", sport.getName());
        requestBody.put("leaderName", user.getFirstName());
        requestBody.put("totalPlayers", 6);
        requestBody.put("teamMembersCount", 6);
        requestBody.put("isValidated", true);
        requestBody.put("ownerId", 1);
        requestBody.put("members", null);

        TeamDto teamDto = new TeamDto();

        teamDto.setId(requestBody.getLong("id"));
        teamDto.setName(requestBody.getString("name"));
        teamDto.setSport(requestBody.getString("sport"));
        teamDto.setLeaderName(requestBody.getString("leaderName"));
        teamDto.setTotalPlayers(requestBody.getInt("totalPlayers"));
        teamDto.setTeamMembersCount(requestBody.getInt("teamMembersCount"));
        teamDto.setValidated(requestBody.getBoolean("isValidated"));
        teamDto.setOwnerId(requestBody.getLong("ownerId"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testUpdateTeam() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 1);
        requestBody.put("name", "TeamUpdate");
        requestBody.put("sport", sport.getName());

        TeamUpdateDto teamUpdateDto = new TeamUpdateDto();

        teamUpdateDto.setId(requestBody.getLong("id"));
        teamUpdateDto.setName(requestBody.getString("name"));
        teamUpdateDto.setSport(requestBody.getString("sport"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/teams/Team")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testDeleteTeam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/teams/TeamUpdate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllTeamsOfUserErrorJWTBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(containsString("error_message\":\"JWT is malformed. Please verify its integrity.")))
                .andReturn();
    }

    @Test
    public void testGetTeamByNonExistingNameBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/TeamUpdated")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Aucune équipe n'a été trouvé avec ce nom: TeamUpdate")))
                .andReturn();
    }

    @Test
    public void testGetTeamsByNonExistingSportBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/sport")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Aucune équipe n'a été trouvé avec ce nom: sport")))
                .andReturn();
    }

    @Test
    public void testCreateTeamUnauthorized() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 2);
        requestBody.put("name", "New Team");
        requestBody.put("sport", sport.getName());
        requestBody.put("leaderName", user.getFirstName());
        requestBody.put("totalPlayers", 6);
        requestBody.put("teamMembersCount", 6);
        requestBody.put("isValidated", true);
        requestBody.put("ownerId", 1);
        requestBody.put("members", null);

        TeamDto teamDto = new TeamDto();

        teamDto.setId(requestBody.getLong("id"));
        teamDto.setName(requestBody.getString("name"));
        teamDto.setSport(requestBody.getString("sport"));
        teamDto.setLeaderName(requestBody.getString("leaderName"));
        teamDto.setTotalPlayers(requestBody.getInt("totalPlayers"));
        teamDto.setTeamMembersCount(requestBody.getInt("teamMembersCount"));
        teamDto.setValidated(requestBody.getBoolean("isValidated"));
        teamDto.setOwnerId(requestBody.getLong("ownerId"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(containsString("{\"error_message\":\"JWT is malformed. Please verify its integrity.\",\"is_jwt_malformed\":\"true\"}")))
                .andReturn();
    }

    @Test
    public void testUpdateNonExistingTeamBadRequest() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 1);
        requestBody.put("name", "TeamUpdate");
        requestBody.put("sport", sport.getName());

        TeamUpdateDto teamUpdateDto = new TeamUpdateDto();

        teamUpdateDto.setId(requestBody.getLong("id"));
        teamUpdateDto.setName(requestBody.getString("name"));
        teamUpdateDto.setSport(requestBody.getString("sport"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/teams/Team1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Aucune équipe n'a été trouvé avec ce nom: Team1")))
                .andReturn();
    }

    @Test
    public void testDeleteNonExistingTeamBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/teams/TeamUpdated")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Aucune équipe n'a été trouvé avec ce nom: TeamUpdated")))
                .andReturn();
    }
}