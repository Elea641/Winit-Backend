package com.cda.winit;

import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.sport.domain.service.SportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
public class SportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SportService sportService;

    @BeforeEach
    public void setUp() {
        Sport sport1 = new Sport(1L, "sport1", "image1", 1, new ArrayList<>());
        Sport sport2 = new Sport(2L, "sport2", "image2", 2, new ArrayList<>());
        sportService.saveSport(sport1);
        sportService.saveSport(sport2);
    }


    @Test
    public void testGetAllSportNames() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sports/names"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"sport1\",\"sport2\"]"));
    }

    @Test
    public void testGetAll() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sports/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{" +
                        "'name':'sport1'," +
                        "'numberOfPlayers': 1," +
                        "'imageUrl': 'image1'" +
                        "}," +
                        "{" +
                        "'name':'sport2'," +
                        "'numberOfPlayers': 2," +
                        "'imageUrl': 'image2'" +
                        "}" +
                        "]"));
    }
}
