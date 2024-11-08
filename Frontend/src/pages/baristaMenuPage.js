import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import drinkIngredientModule from "../api/drinkIngredientModule"

class BaristaMenuPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'renderDrinkOptions', 'renderIngredients', 'renderRecipe'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('display-drink-form').addEventListener('change', this.renderIngredients);
        document.getElementById('display-drink-form').addEventListener('change', this.renderRecipe);

        await this.onGet();
        this.dataStore.addChangeListener(this.renderDrinkOptions);
    }

    async onGet() {
        try {
            const drinks = await drinkIngredientModule.getAllDrinks();
            this.dataStore.set("drinks", drinks);
        } catch (error) {
            console.error("Failed to load drinks:", error);
        }
    }

    async renderDrinkOptions() {
        const drinks = this.dataStore.get("drinks");
        const renderArea = document.getElementById("drink-options");


        if (drinks && drinks.length > 0) {
            renderArea.innerHtml = drinks.map(drink => `<option value="${drink.id}">${drink.name}</option>`).join('');
        } else {
            renderArea.innerHtml = "<p>No drinks available</p>";
        }
    }

    async renderIngredients() {
        const drinks = this.dataStore.get("drinks");
        const selectedValue = drinkIngredientModule.getIngredientById(document.getElementById("drink-options").value);
        const selectedDrink = drinks.find(drink => drink.id === selectedValue);

        const ingredients = document.getElementById("ingredient-list");
        if (selectedDrink && selectedDrink.ingredients) {
            ingredients.innerHTML = selectedDrink.ingredients.map(ingredient => `<li>${ingredient.name}</li>`).join('');
        } else {
            ingredients.innerHTML = "<li>No ingredients found</li>";
        }
    }


    async renderRecipe() {
        const drinks = this.dataStore.get("drinks");
        const selectedDrinkId = drinkIngredientModule.getIngredientById(document.getElementById("drink-options").value);
        const selectedDrink = drinks.find(drink => drink.id === selectedDrinkId);
        const recipeElement = document.getElementById("drink-recipe");
        recipeElement.innerHTML = selectedDrink && selectedDrink.recipe ? selectedDrink.recipe : "No recipe available";
    }
}
const main = async () => {
    const baristaMenuPage = new BaristaMenuPage();
    await baristaMenuPage.mount();
}

window.addEventListener('DOMContentLoaded', main);
