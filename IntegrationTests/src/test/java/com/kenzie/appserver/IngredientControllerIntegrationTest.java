package com.kenzie.appserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.IngredientCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import java.util.List;

@IntegrationTest
@ContextConfiguration(initializers = { DynamoDbInitializer.class })
public class IngredientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private IngredientCreateRequest validIngredientRequest;
    private IngredientCreateRequest invalidIngredientRequest;

    @BeforeEach
    void setUp() {
        validIngredientRequest = new IngredientCreateRequest(
                "1", "Sugar", 100
        );

        invalidIngredientRequest = new IngredientCreateRequest(
                "", "Invalid Ingredient", -50
        );
    }

    @Test
    void addIngredient_success() throws Exception {
        String requestBody = objectMapper.writeValueAsString(validIngredientRequest);

        mockMvc.perform(post("/ingredients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sugar"))
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    void addIngredient_fail() throws Exception {
        String requestBody = objectMapper.writeValueAsString(invalidIngredientRequest);

        mockMvc.perform(post("/ingredients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllIngredients_success() throws Exception {
        mockMvc.perform(get("/ingredients/all")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getIngredientById_success() throws Exception {
        String ingredientId = validIngredientRequest.getId();

        mockMvc.perform(get("/ingredients/{id}" + ingredientId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ingredientId));
    }

    @Test
    void getIngredientById_fail() throws Exception {
        mockMvc.perform(get("/ingredients/{id}", invalidIngredientRequest.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
