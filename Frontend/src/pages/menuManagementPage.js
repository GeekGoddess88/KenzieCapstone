import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import drinkIngredientModule from "../api/drinkIngredientModule";

class MenuManagementPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onGet', 'onDelete', 'onUpdate', 'renderDrinkList', 'renderIngredientList'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('create-drink-form').addEventListener('submit', this.onCreate);
        document.getElementById('update-drink-form').addEventListener('submit', this.onUpdate);
        document.getElementById('remove-drink-form').addEventListener('submit', this.onDelete);

        await this.onGet();
        this.dataStore.addChangeListener(this.renderDrinkList);
        this.dataStore.addChangeListener(this.renderIngredientList);

    }

    async onGet() {
        const drinks = await drinkIngredientModule.getAllDrinks();
        this.dataStore.set("drinks", drinks);
        const ingredients = await drinkIngredientModule.getAllIngredients();
        this.dataStore.set("ingredients", ingredients);
    }

    async onCreate(event) {
        event.preventDefault();

        const name = document.getElementById("create-drink-name").value;
        const recipe = document.getElementById("create-drink-recipe").value;
        const ingredients = Array.from(document.querySelectorAll("input[name='ingredient-checkbox']:checked"))
            .map(checkbox => checkbox.value);

        const newDrink = await drinkIngredientModule.addDrink({ name, ingredients, recipe });
        if (newDrink) {
            this.showMessage(`${newDrink.name} added to the menu.`);
            await this.onGet();
        }
    }

    async onUpdate(event) {
        event.preventDefault();
        const drinkId = document.getElementById("update-drink-select").value;
        const name = document.getElementById("update-drink-name").value;
        const recipe = document.getElementById("update-drink-recipe").value;
        const ingredients = Array.from(document.querySelectorAll("input[name='update-ingredient-checkbox']:checked"))
            .map(checkbox => checkbox.value);

        const updatedDrink = await drinkIngredientModule.updateDrink(drinkId, { name, ingredients, recipe });
        if (updatedDrink) {
            this.showMessage(`${updatedDrink.name} updated.`);
            await this.onGet();
        }
    }

    async onDelete(event) {
        event.preventDefault();
        const drinkId = document.getElementById("delete-drink-select").value;
        const deletedDrink = await drinkIngredientModule.deleteDrink(drinkId);
        if (deletedDrink) {
            this.showMessage("Drink removed.");
            await this.onGet();
        }
    }

    // Render method

    renderDrinkList() {
        const drinks = this.dataStore.get("drinks") || [];
        const deleteArea = document.getElementById("delete-drink-select");
        const updateArea = document.getElementById("update-drink-select");

        if (drinks) {
            const optionsHtml = drinks.map(drink => `<option value="${drink.id}">${drink.name}</option>`).join("");
            deleteArea.innerHTML = optionsHtml;
            updateArea.innerHTML = optionsHtml;
        }
    }

    renderIngredientList() {
        const ingredients = this.dataStore.get("ingredients") || [];
        const createIngredientArea = document.getElementById("ingredients-in-create-form");
        const updateIngredientArea = document.getElementById("ingredients-in-update-form");

        createIngredientArea.innerHTML = ingredients.map(ingredient => `<label><input type="checkbox" name="ingredient-checkbox" value="${ingredient.id}"> ${ingredient.name}</label>`).join("<br>");

        updateIngredientArea.innerHTML = ingredients.map(ingredient => `<label><input type="checkbox" name="update-ingredient-checkbox" value="${ingredient.id}"> ${ingredient.name}</label>`).join("<br>");
    }

}

const main = async () => {
    const menuManagementPage = new MenuManagementPage();
    await menuManagementPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
