package com.cda.winit;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegister() throws Exception {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("createdAt", LocalDateTime.now());
        jsonUser.put("lastName", "daa");
        jsonUser.put("firstName", "jane");
        jsonUser.put("password", "1234");
        jsonUser.put("email", "jane@doe.com");
        jsonUser.put("enabled", true);
        jsonUser.put("role", "USER_ROLE");

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(jsonUser.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testLogin() throws Exception {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("email", "john@doe.com");
        jsonUser.put("password", "johndoe");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(jsonUser.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                ;

        assertNotNull(result.getResponse().getContentType());
    }
}
