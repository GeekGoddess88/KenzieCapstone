package com.kenzie.capstone.service;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/
/*
    private IngredientDAO exampleDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.exampleDao = mock(IngredientDAO.class);
        this.lambdaService = new LambdaService(exampleDao);
    }

    @Test
    void setDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String data = "somedata";

        // WHEN
        IngredientDAO response = this.lambdaService.setExampleData(data);

        // THEN
        verify(exampleDao, times(1)).setId(idCaptor.capture(), dataCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(data, dataCaptor.getValue(), "The data is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
        assertEquals(data, response.getId(), "The response data should match");
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String data = "somedata";
        IngredientRecord record = new IngredientRecord();
        record.setId(id);
        record.setName(data);


        when(exampleDao.getId(id)).thenReturn(Arrays.asList(record));

        // WHEN
        IngredientDAO response = this.lambdaService.getExampleData(id);

        // THEN
        verify(exampleDao, times(1)).getId(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(data, response.getId(), "The response data should match");
    }

    // Write additional tests here
*/
}
