package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.DrinkCreateRequest;
import com.kenzie.appserver.controller.model.DrinkResponse;
import com.kenzie.appserver.controller.model.DrinkUpdateRequest;
import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.model.Drink;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/drink")
public class DrinkController {

    private DrinkRepository drinkRepository;
    private DrinkService drinkService;

    DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkResponse> getDrink(@PathVariable("id") String id) {
        Drink drink = drinkService.findById(id);
        if (drink == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drinkToResponse(drink));
    }
    @GetMapping("/all")
    public ResponseEntity<List<DrinkResponse>> getDrink() {
        List<Drink> drinks = drinkService.findAll();
        List<DrinkResponse> drinkResponses = drinks.stream().map(drink -> drinkToResponse(drink)).collect(Collectors.toList());
        return ResponseEntity.ok(drinkResponses);
    }
    @PostMapping
    public ResponseEntity<DrinkResponse> addDrink(@RequestBody DrinkCreateRequest drinkCreateRequest) {
        Drink drink = new Drink(randomUUID().toString(),
                drinkCreateRequest.getName(),
                drinkCreateRequest.getIngredients(),
                drinkCreateRequest.getRecipe());
        drinkService.addDrink(drink);

        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drink.getId());
        drinkResponse.setName(drink.getName());
        drinkResponse.setIngredients(drink.getIngredients());
        drinkResponse.setRecipe(drink.getRecipe());

        return ResponseEntity.created(URI.create("/drink/" + drinkResponse.getId())).body(drinkResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DrinkResponse> updateDrink(@PathVariable("id") String id, @RequestBody DrinkUpdateRequest drinkUpdateRequest) {
        Drink drinkRecord = drinkService.findById(id);
//                .orElseThrow(() -> new ResourceNotFoundException("Drink with id " + id + " not found."));
        Drink drinkUpdate = new Drink(drinkRecord.getId(),
                drinkUpdateRequest.getName(),
                drinkUpdateRequest.getIngredients(),
                drinkUpdateRequest.getRecipe());
        drinkService.updateDrink(id, drinkUpdate);

        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drinkUpdate.getId());
        drinkResponse.setName(drinkUpdate.getName());
        drinkResponse.setIngredients(drinkUpdate.getIngredients());
        drinkResponse.setRecipe(drinkUpdate.getRecipe());

        return ResponseEntity.ok(drinkResponse);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteDrink(@PathVariable("id") String id) {
        drinkService.deleteDrink(id);
        return ResponseEntity.ok("Drink removed.");
    }

    // Convert DrinkRecord to DrinkResponse helper method
    private DrinkResponse drinkToResponse(Drink drink) {
        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setId(drink.getId());
        drinkResponse.setName(drink.getName());
        drinkResponse.setIngredients(drink.getIngredients());
        drinkResponse.setRecipe((drink.getRecipe()));
        return drinkResponse;
    }
}
