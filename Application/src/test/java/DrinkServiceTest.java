import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DrinkServiceTest {

    @Mock
    private DrinkRepository drinkRepository;

    @Mock
    private LambdaServiceClient lambdaServiceClient;

    @InjectMocks
    private DrinkService drinkService;
    private DrinkCreateRequest drinkCreateRequest;
    private DrinkResponse drinkResponse;
    private DrinkRecord drinkRecord;

    @BeforeEach
    void setUp() {
        drinkCreateRequest = new DrinkCreateRequest("1", "Espresso", "Brew coffee and enjoy!", List.of(
                new IngredientCreateRequest("2", "Water", 50),
                new IngredientCreateRequest("3", "Coffee Beans", 100)
        ));

        drinkRecord = new DrinkRecord("1", "Espresso", List.of(
                new IngredientRecord("2", "Water", 50),
                new IngredientRecord("3", "Coffee Beans", 100)
        ), "Brew coffee and enjoy");

        drinkResponse = new DrinkResponse("1", "Espresso", List.of(
                new IngredientResponse("2", "Water", 50),
                new IngredientResponse("3", "Coffee Beans", 100)
        ), "Brew coffee and enjoy");
    }

    @Test
    void addDrinkTest_Saves() throws Exception {
        when(lambdaServiceClient.addDrink(drinkCreateRequest)).thenReturn(CompletableFuture.completedFuture(drinkResponse));
        when(drinkRepository.save(any(DrinkRecord.class))).thenReturn(drinkRecord);

        CompletableFuture<DrinkResponse> response = drinkService.addDrink(drinkCreateRequest);

        assertNotNull(response);
        assertEquals("Espresso", response.get().getName());
        verify(drinkRepository, times(1)).save(any(DrinkRecord.class));
    }

    @Test
    void addDrinkTest_NotSave() {
        when(lambdaServiceClient.addDrink(drinkCreateRequest)).thenThrow(new IOException("Failed to add drink"));
        assertThrows(RuntimeException.class, () -> drinkService.addDrink(drinkCreateRequest));
        verify(drinkRepository, times(0)).save(any(DrinkRecord.class));
    }

    @Test
    void getDrinkTest_ReturnDrink() {
        when(drinkRepository.findById("1")).thenReturn(Optional.of(drinkRecord));

        CompletableFuture<DrinkResponse> response = drinkService.getDrinkById("1");

        assertNotNull(response);
        assertEquals("Espresso", response.join().getName());
    }

    @Test
    void getDrinkTest_NotFound() {
        when(drinkRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> drinkService.getDrinkById("1").join());
    }
}
