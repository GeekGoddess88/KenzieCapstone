package com.kenzie.appserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.DrinkCreateRequest;
import com.kenzie.capstone.service.model.IngredientCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ContextConfiguration(initializers = { DynamoDbInitializer.class })
class DrinkControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private DrinkCreateRequest validDrinkRequest;
    private DrinkCreateRequest invalidDrinkRequest;

    @BeforeEach
    void setUp() {
        IngredientCreateRequest ingredient1 = new IngredientCreateRequest("1", "Coffee Beans", 50);
        IngredientCreateRequest ingredient2 = new IngredientCreateRequest("2", "Water", 200);
        IngredientCreateRequest ingredient3 = new IngredientCreateRequest("3", "Milk", 100);

        validDrinkRequest = new DrinkCreateRequest(
                "1",
                "Espresso",
                "Brew coffee and enjoy!",
                List.of(ingredient1, ingredient2, ingredient3)
        );

        invalidDrinkRequest = new DrinkCreateRequest(
                "",
                "An empty drink",
                "",
                List.of()
        );
    }

    @Test
    void addDrink_success() throws Exception {
        String requestBody = objectMapper.writeValueAsString(validDrinkRequest);

        mockMvc.perform(post("/drinks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Espresso"))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients.length()").value(3));
    }

    @Test
    void addDrink_failure() throws Exception {
        String requestBody = objectMapper.writeValueAsString(invalidDrinkRequest);

        mockMvc.perform(post("/drinks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .requestAttr("drink", invalidDrinkRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllDrinks_success() throws Exception {
        mockMvc.perform(get("/drinks/all")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void getDrinkById_success() throws Exception {
        String drinkId = "valid-drink-id";

        mockMvc.perform(get("/drinks/{id}", drinkId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(drinkId));
    }

    @Test
    void getDrinkById_failure() throws Exception {
        mockMvc.perform(get("/drinks/{id}", invalidDrinkRequest.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}


