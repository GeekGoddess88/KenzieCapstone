
import com.kenzie.appserver.repositories.IngredientRepository;
import com.kenzie.appserver.service.IngredientService;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.IngredientCreateRequest;
import com.kenzie.capstone.service.model.IngredientRecord;
import com.kenzie.capstone.service.model.IngredientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private LambdaServiceClient lambdaServiceClient;

    @InjectMocks
    private IngredientService ingredientService;

    private IngredientCreateRequest ingredientCreateRequest;
    private IngredientResponse ingredientResponse;
    private IngredientRecord ingredientRecord;

    @BeforeEach
    void setUp() {
        ingredientCreateRequest = new IngredientCreateRequest("2", "Water", 50);
        ingredientRecord = new IngredientRecord("2", "Water", 50);
        ingredientResponse = new IngredientResponse("2", "Water", 50);
    }

    @Test
    void addIngredientTest_SavesIngredient() throws Exception {
        when(lambdaServiceClient.addIngredient(ingredientCreateRequest))
                .thenReturn(CompletableFuture.completedFuture(ingredientResponse));
        when(ingredientRepository.save(any(IngredientRecord.class))).thenReturn(ingredientRecord);

        CompletableFuture<IngredientResponse> response = ingredientService.addIngredient(ingredientCreateRequest);

        assertNotNull(response);
        assertEquals("Water", response.get().getName());
        verify(ingredientRepository, times(1)).save(any(IngredientRecord.class));
    }

    @Test
    void addIngredientTest_NotSaveIngredient() {
        when(lambdaServiceClient.addIngredient(ingredientCreateRequest)).thenThrow(new IOException("Failed to add ingredient"));

        assertThrows(RuntimeException.class, () -> ingredientService.addIngredient(ingredientCreateRequest));
        verify(ingredientRepository, never()).save(any(IngredientRecord.class));
    }

    @Test
    void getIngredientTest_ReturnsIngredient() {
        when(ingredientRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> ingredientService.getIngredientById("2").join());
    }
}
