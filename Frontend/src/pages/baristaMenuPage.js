import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import DrinkClient from "../api/drinkClient";
import IngredientClient from "../api/ingredientClient";

class BaristaMenuPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'renderDrinkList', 'renderIngredients', 'renderRecipe'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('display-drink-form').addEventListener('submit', this.onCreate);
        document.getElementById('display-drink-form').addEventListener('submit', this.renderIngredients);
        document.getElementById('display-drink-form').addEventListener('submit', this.renderRecipe);
        this.drinkClient = new DrinkClient();
//        this.ingredientClient = new IngredientClient();
        this.onGet();
        this.dataStore.addChangeListener(this.renderDrinkList);
//        this.dataStore.addChangeListener(this.renderIngredients);
//        this.dataStore.addChangeListener(this.renderRecipe);
    }
    // Render Methods

    async renderDrinkList() {
//        event.preventDefault();
        const drinks = this.dataStore.get("drink");
        let renderArea = document.getElementById("display-drink-form");


        if (drinks.length > 0) {
            renderArea = document.getElementById("drinks-in-menu");
            let myHtml = "<select id=select-options>"
            for (let drink of drinks) {
                myHtml += "<option value=" + drink.id + ">" + drink.name + "</option>"
            }
            myHtml += "</select><p></p><button type=submit>Select</button>";

            renderArea.innerHTML = myHtml;
        } else {
            renderArea.innerHTML = "No Drinks in Menu";
        }
    }

    async renderIngredients() {
        event.preventDefault();
        let renderArea = document.getElementById("display-drink-ingredients");

        const drinks = this.dataStore.get("drink");
        let selectedValue = document.getElementById('select-options').value;
        for (let drink of drinks) {
            if (drink.id == selectedValue) {
                var selectedDrink = drink;
            }
        }
        let ingredients = selectedDrink.ingredients;
        let ingredientNames = [];
        for (let i of ingredients) {
            ingredientNames.push(i.name);
        }
        let myHtml = "<ul>";
        for (let i of ingredientNames) {
            myHtml += "<li>" + i + "</li>"
        }
        myHtml += "</ul>"

        renderArea.innerHTML = myHtml;
    }

    async renderRecipe() {
        event.preventDefault();
        let renderArea = document.getElementById("display-drink-recipe");

        const drinks = this.dataStore.get("drink");
        let selectedValue = document.getElementById('select-options').value;
        for (let drink of drinks) {
            if (drink.id == selectedValue) {
                var selectedDrink = drink;
            }
        }
        let recipe = selectedDrink.recipe;
        renderArea.innerHTML = recipe;
    }

    // Event Handlers

    async onGet(event) {
            let result = await this.drinkClient.getAllDrinks(this.errorHandler);
            this.dataStore.set("drink", result);
        }

}
const main = async () => {
    const baristaMenuPage = new BaristaMenuPage();
    baristaMenuPage.mount();
}

window.addEventListener('DOMContentLoaded', main);
