package com.kenzie.appserver;

import com.kenzie.appserver.repositories.IngredientRepository;
import com.kenzie.appserver.service.IngredientService;
import com.kenzie.capstone.service.model.IngredientCreateRequest;
import com.kenzie.capstone.service.model.IngredientRecord;
import com.kenzie.capstone.service.model.IngredientResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IngredientServiceIntegrationTest {
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll();
    }

    @Test
    void addIngredient_Success() throws Exception {
        IngredientCreateRequest request = new IngredientCreateRequest("1", "Sugar", 5);
        IngredientResponse response = ingredientService.addIngredient(request).join();

        Assertions.assertNotNull(response);
        assertEquals("Sugar", response.getName());
    }

    @Test
    void addIngredient_Failure() throws Exception {
        IngredientCreateRequest request = new IngredientCreateRequest("2", "", 5);

        assertThrows(RuntimeException.class, () -> ingredientService.addIngredient(request).join());
    }

    @Test
    void getIngredientById_Success() throws Exception {
        IngredientRecord ingredient = new IngredientRecord("1", "Salt", 10);
        ingredientRepository.save(ingredient);

        IngredientResponse response = ingredientService.getIngredientById(ingredient.getId()).join();

        assertNotNull(response);
        assertEquals("Salt", response.getName());
    }

    @Test
    void getIngredientById_Failure() throws Exception {
        assertThrows(RuntimeException.class, () -> ingredientService.getIngredientById("99").join());
    }

    @Test
    void getAllIngredients_Success() throws Exception {
        ingredientRepository.save(new IngredientRecord("1", "Milk", 20));
        ingredientRepository.save(new IngredientRecord("2", "Chocolate", 15));

        List<IngredientResponse> ingredients = ingredientService.getAllIngredients().join();

        assertEquals(2, ingredients.size());
    }

    @Test
    void getAllIngredients_Failure() throws Exception {
        List<IngredientResponse> ingredients = ingredientService.getAllIngredients().join();

        assertTrue(ingredients.isEmpty());
    }
}
