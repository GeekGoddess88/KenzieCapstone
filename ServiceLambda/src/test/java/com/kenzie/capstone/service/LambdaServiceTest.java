package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.model.converter.DrinkConverter;
import com.kenzie.capstone.service.model.converter.IngredientConverter;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    @Mock
    private DrinkDAO drinkDAO;

    @Mock
    private IngredientDAO ingredientDAO;

    @Mock
    DrinkConverter drinkConverter;

    @Mock
    IngredientConverter ingredientConverter;

    @InjectMocks
    private LambdaService lambdaService;
    private DrinkRecord drinkRecord;
    private DrinkCreateRequest drinkCreateRequest;
    private DrinkResponse drinkResponse;
    private DrinkUpdateRequest drinkUpdateRequest;
    private IngredientRecord ingredientRecord;
    private IngredientCreateRequest ingredientCreateRequest;
    private IngredientResponse ingredientResponse;
    private IngredientUpdateRequest ingredientUpdateRequest;


    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        IngredientCreateRequest waterRequest = new IngredientCreateRequest("1", "Water", 500);
        IngredientCreateRequest beansRequest = new IngredientCreateRequest("2", "Coffee Beans", 100);
        IngredientCreateRequest cremaRequest = new IngredientCreateRequest("3", "Crema", 50);

        List<IngredientCreateRequest> ingredientCreateRequests = Arrays.asList(waterRequest, beansRequest, cremaRequest);
        drinkCreateRequest = new DrinkCreateRequest("1", "Espresso", "Espresso Recipe", ingredientCreateRequests);

        IngredientRecord waterRecord = new IngredientRecord("1", "Water", 500);
        IngredientRecord beansRecord = new IngredientRecord("2", "Coffee Beans", 100);
        IngredientRecord cremaRecord = new IngredientRecord("3", "Crema", 50);
        List<IngredientRecord> ingredientRecords = Arrays.asList(waterRecord, beansRecord, cremaRecord);

        IngredientResponse waterResponse = new IngredientResponse("1", "Water", 500);
        IngredientResponse beansResponse = new IngredientResponse("2", "Coffee Beans", 100);
        IngredientResponse cremaResponse = new IngredientResponse("3", "Crema", 50);
        List<IngredientResponse> ingredientResponses = Arrays.asList(waterResponse, beansResponse, cremaResponse);

        ingredientUpdateRequest = new IngredientUpdateRequest("ingredient2", "Decaf Coffee Beans", 100);
        ingredientResponse = new IngredientResponse("1", "Water", 500);
        ingredientRecord = new IngredientRecord("1", "Water", 500);
        ingredientCreateRequest = new IngredientCreateRequest("1", "Water", 500);
        drinkUpdateRequest = new DrinkUpdateRequest("drink2", "Updated Espresso", "Mix in crema with brewed coffee and enjoy!", ingredientResponses);
        drinkRecord = new DrinkRecord("drink1", "Espresso", ingredientRecords, "Brew coffee and enjoy!");
        drinkResponse = new DrinkResponse("drink1", "Espresso", ingredientResponses, "Brew the coffee and enjoy!");
        lambdaService = new LambdaService(drinkDAO, ingredientDAO, drinkConverter, ingredientConverter);
    }

    @Test
    void addDrinkTest() throws Exception {
        when(drinkConverter.toDrinkRecord(drinkCreateRequest)).thenReturn(drinkRecord);
        doNothing().when(drinkDAO).save(drinkRecord);
        when(drinkConverter.toDrinkResponse(drinkRecord)).thenReturn(drinkResponse);

        DrinkResponse drinkResponse = lambdaService.addDrink(drinkCreateRequest);
        assertNotNull(drinkResponse);
        assertEquals("Espresso", drinkResponse.getName());
        assertEquals(3, drinkResponse.getIngredients().size());
        verify(drinkDAO, times(1)).save(any(DrinkRecord.class));
    }

    @Test
    void getDrinkByIdTest() throws Exception {
        when(drinkDAO.findById("drink1")).thenReturn(Optional.of(drinkRecord));
        when(drinkConverter.toDrinkResponse(drinkRecord)).thenReturn(drinkResponse);

        DrinkResponse response = lambdaService.getDrinkById("drink1");
        assertNotNull(response);
        assertEquals("Espresso", response.getName());
        assertEquals(3, response.getIngredients().size());
        verify(drinkDAO, times(1)).findById("drink1");
    }

    @Test
    void getAllDrinksTest() throws Exception {
        List<IngredientRecord> ingredientRecords = Arrays.asList(
                new IngredientRecord("1", "Water", 500),
                new IngredientRecord("2", "Coffee Beans", 200)
        );

        List<DrinkRecord> drinkRecords = Arrays.asList(
                new DrinkRecord("drink1", "Espresso", ingredientRecords, "Brew coffee and enjoy!"),
                new DrinkRecord("drink2", "Latte", ingredientRecords, "Brew coffee mix with milk and enjoy!")
        );

        when(drinkDAO.findAll()).thenReturn(drinkRecords);
        when(drinkConverter.toDrinkResponse(any(DrinkRecord.class)))
                .thenAnswer(invocation -> {
                    DrinkRecord record = (DrinkRecord) invocation.getArguments()[0];
                    List<IngredientResponse> ingredientResponses = record.getIngredients().stream()
                            .map(ingredientRecord -> new IngredientResponse(
                                    ingredientRecord.getId(),
                                    ingredientRecord.getName(),
                                    ingredientRecord.getQuantity()))
                            .collect(Collectors.toList());
                    return new DrinkResponse(record.getId(), record.getName(), ingredientResponses, record.getRecipe());
                });

        List<DrinkResponse> drinkResponses = lambdaService.getAllDrinks();

        assertNotNull(drinkResponses);
        assertEquals(2, drinkResponses.size());
        assertEquals("Espresso", drinkResponses.get(0).getName());
        assertEquals("Latte", drinkResponses.get(1).getName());
        assertEquals(2, drinkResponses.get(0).getIngredients().size());
        assertEquals("Water", drinkResponses.get(0).getIngredients().get(0).getName());
        assertEquals("Coffee Beans", drinkResponses.get(0).getIngredients().get(1).getName());
        verify(drinkDAO, times(1)).findAll();
    }

    @Test
    void updateDrinkTest() throws Exception {
        when(drinkConverter.toDrinkRecord(drinkUpdateRequest)).thenReturn(drinkRecord);

        doNothing().when(drinkDAO).update("drink1", drinkRecord);
        when(drinkConverter.toDrinkResponse(drinkRecord)).thenReturn(drinkResponse);

        DrinkResponse response = lambdaService.updateDrink("1", drinkUpdateRequest);

        assertNotNull(response);
        assertEquals("Espresso", response.getName());
        verify(drinkDAO, times(1)).update(eq("drink1"),any(DrinkRecord.class));
    }

    @Test
    void deleteDrinkTest() throws Exception {
        doNothing().when(drinkDAO).delete("drink1");
        DeleteDrinkResponse deleteDrinkResponse = lambdaService.deleteDrinkById("drink1");
        assertNotNull(deleteDrinkResponse);
        verify(drinkDAO, times(1)).delete("1");
    }

    @Test
    void addIngredientTest() throws Exception {
        when(ingredientConverter.toIngredientRecordFromCreateRequest(ingredientCreateRequest)).thenReturn(ingredientRecord);
        doNothing().when(ingredientDAO).save(ingredientRecord);
        when(ingredientConverter.toIngredientResponse(ingredientRecord)).thenReturn(ingredientResponse);

        IngredientResponse response = lambdaService.addIngredient(ingredientCreateRequest);

        assertNotNull(response);
        assertEquals("Water", response.getName());
        assertEquals(500, response.getQuantity());
        verify(ingredientDAO, times(1)).save(any(IngredientRecord.class));
    }

    @Test
    void getIngredientByIdTest() throws Exception {
        when(ingredientDAO.findById("1")).thenReturn(Optional.of(ingredientRecord));
        when(ingredientConverter.toIngredientResponse(ingredientRecord)).thenReturn(ingredientResponse);

        IngredientResponse response = lambdaService.getIngredientById("1");

        assertNotNull(response);
        assertEquals("Water", response.getName());
        assertEquals(500, response.getQuantity());
        verify(ingredientDAO, times(1)).findById("1");
    }

    @Test
    void getAllIngredientsTest() throws Exception {
        List<IngredientRecord> ingredientRecords = Arrays.asList(
                new IngredientRecord("1", "Water", 500),
                new IngredientRecord("2", "Coffee Beans", 200)
        );

        when(ingredientDAO.findAll()).thenReturn(ingredientRecords);

        when(ingredientConverter.toIngredientResponse(any(IngredientRecord.class)))
                .thenAnswer(invocation -> {
                    IngredientRecord record = (IngredientRecord) invocation.getArguments()[0];
                    return new IngredientResponse(record.getId(), record.getName(), record.getQuantity());
                });

        List<IngredientResponse> ingredientResponses = lambdaService.getAllIngredients();

        assertNotNull(ingredientResponses);
        assertEquals(2, ingredientResponses.size());
        assertEquals("Water", ingredientResponses.get(0).getName());
        assertEquals("Coffee Beans", ingredientResponses.get(1).getName());
        assertEquals(200, ingredientResponses.get(0).getQuantity());
        verify(ingredientDAO, times(1)).findAll();
    }

    @Test
    void updateIngredientTest() throws Exception {
        when(ingredientConverter.toIngredientRecordFromUpdateRequest(ingredientUpdateRequest)).thenReturn(ingredientRecord);
        doNothing().when(ingredientDAO).update("ingredient2", ingredientRecord);
        when(ingredientConverter.toIngredientResponse(ingredientRecord)).thenReturn(ingredientResponse);

        IngredientResponse response = lambdaService.updateIngredient("ingredient2", ingredientUpdateRequest);

        assertNotNull(response);
        assertEquals("Water", response.getName());
        assertEquals(500, response.getQuantity());
        verify(ingredientDAO, times(1)).update(eq("ingredient2"),any(IngredientRecord.class));
    }

    @Test
    void deleteIngredientTest() throws Exception {
        doNothing().when(ingredientDAO).delete("1");

        DeleteIngredientResponse deleteIngredientResponse = lambdaService.deleteIngredientById("1");

        assertNotNull(deleteIngredientResponse);
        verify(ingredientDAO, times(1)).delete("1");
    }
}
