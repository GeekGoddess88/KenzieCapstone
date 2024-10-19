package com.kenzie.appserver;

import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.capstone.service.model.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
public class DrinkServiceIntegrationTest {

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkRepository drinkRepository;




    @BeforeEach
    void setUp() {
        drinkRepository.deleteAll();
    }

    @Test
    void addDrink_Success() throws Exception {
        DrinkCreateRequest request = new DrinkCreateRequest(
                "1","Espresso", "Simple Espresso",
                List.of(new IngredientCreateRequest("1", "Coffee Beans", 10)
        ));

        DrinkResponse response = drinkService.addDrink(request).join();

        Assertions.assertNotNull(response);
        assertEquals("Espresso", response.getName());
        assertEquals(1, response.getIngredients().size());
    }

    @Test
    void addDrink_Failure() throws Exception {
        DrinkCreateRequest request = new DrinkCreateRequest(
                "1","Espresso", "Simple espresso",
                List.of(new IngredientCreateRequest("2", "", 10))
        );

        assertThrows(RuntimeException.class, () -> drinkService.addDrink(request).join());
    }

    @Test
    void getDrinkById_Success() throws Exception {
        DrinkRecord drink = new DrinkRecord("1", "Americano",
                List.of(new IngredientRecord("1", "Water", 10)), "Simple recipe");
        drinkRepository.save(drink);

        DrinkResponse drinkResponse = drinkService.getDrinkById(drink.getId()).join();

        Assertions.assertNotNull(drinkResponse);
        assertEquals("Americano", drinkResponse.getName());
    }

    @Test
    void getDrinkById_Failure() throws Exception {
        assertThrows(RuntimeException.class, () -> drinkService.getDrinkById("99").join());
    }

    @Test
    void getAllDrinks_Success() throws Exception {
        drinkRepository.save(new DrinkRecord("1", "Latte",
                List.of(new IngredientRecord("1", "Milk", 20)), "Delicious Drink"));
        drinkRepository.save(new DrinkRecord("2", "Mocha",
                List.of(new IngredientRecord("2", "Chocolate", 15)), "Delicious Drink"));

        List<DrinkResponse> drinks = drinkService.getAllDrinks().join();

        assertEquals(2, drinks.size());
    }

    @Test
    void getAllDrinks_Failure() throws Exception {
        List<DrinkResponse> drinks = drinkService.getAllDrinks().join();

        assertTrue(drinks.isEmpty());
    }
}


