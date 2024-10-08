import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
//import DrinkClient from "../api/drinkClient";
import IngredientClient from "../api/ingredientClient";

class InventoryManagementPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onGet', 'onDelete', 'onUpdate', 'onSelectIngredient',
            'renderDeleteIngredientList', 'renderUpdateIngredientList'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('create-ingredient-form').addEventListener('submit', this.onCreate);
        document.getElementById('update-ingredient-form').addEventListener('submit', this.onUpdate);
        document.getElementById('remove-ingredient-form').addEventListener('submit', this.onDelete);
//        this.drinkClient = new DrinkClient();
        this.ingredientClient = new IngredientClient();



        this.onGet();
        this.dataStore.addChangeListener(this.renderDeleteIngredientList);
        this.dataStore.addChangeListener(this.renderUpdateIngredientList);
    }

    // Render method

    async renderDeleteIngredientList() {
        const ingredients = this.dataStore.get("ingredient");
        let renderArea = document.getElementById("remove-ingredient-form");


        if (ingredients.length > 0) {
            renderArea = document.getElementById("ingredients-in-menu");
            let myHtml = "<select id=delete-options>"
            for (let ing of ingredients) {
                myHtml += "<option value=" + ing.id + ">" + ing.name + "</option>"
            }
            myHtml += "</select><p></p><button type=submit class=form-button>Remove</button>";

            renderArea.innerHTML = myHtml;
        } else {
            renderArea.innerHTML = "No Ingredients in Menu";
        }
    }

    async renderUpdateIngredientList() {
        const ingredients = this.dataStore.get("ingredient");
        let renderArea = document.getElementById("ingredients-in-update-form");


        if (ingredients.length > 0) {
            renderArea = document.getElementById("ingredients-in-update-form");
            let myHtml = "<select id=ingredient-update-options>"
            for (let ing of ingredients) {
                myHtml += "<option value=" + ing.id + ">" + ing.name + "</option>"
            }
            myHtml += "</select><p></p>";

            renderArea.innerHTML = myHtml;
        } else {
            renderArea.innerHTML = "No Ingredients to Select";
        }
    }

    // Event handlers

    async onSelectIngredient(event) {
        let selectedValue = document.getElementById("ingredient-options").value;
        this.ingredientChoices.push(selectedValue);
        console.log(ingredientChoices);
    }

    async onDelete(event) {
        event.preventDefault();
        let selectedValue = document.getElementById('delete-options').value;
        let ingredientToDelete = await this.ingredientClient.getIngredient(selectedValue, this.errorHandler);
        await this.ingredientClient.deleteIngredient(selectedValue, this.errorHandler());
        this.showMessage(`${ingredientToDelete.name} has been removed.`)
    }

    async onGet(event) {
        let result = await this.ingredientClient.getAllIngredients(this.errorHandler);
        this.dataStore.set("ingredient", result);
    }

    async onCreate(event) {
        event.preventDefault();
        let backendIngredientList = await this.ingredientClient.getAllIngredients(this.errorHandler);

        let name = document.getElementById("create-ingredient-name").value;

        let quantity = document.getElementById("create-ingredient-quantity").value;

        const createdIngredient = await this.ingredientClient.addIngredient(name, quantity, this.errorHandler);

        if (createdIngredient) {
            this.showMessage(`${createdIngredient.name} has been added to the menu.`)
        } else {
            this.errorHandler("Ingredient creation error");
        }
        this.onGet();
        window.location.href = window.location.href;
    }

    async onUpdate(event) {
        event.preventDefault();
        let selection = document.getElementById('ingredient-update-options').value;
        let ingredientToUpdate = await this.ingredientClient.getIngredient(selection, this.errorHandler);

        let id = ingredientToUpdate.id;
        let name = ingredientToUpdate.name;
        let quantity = document.getElementById("update-ingredient-quantity").value;

        const updatedDrink = await this.ingredientClient.updateIngredient(id, name, quantity, this.errorHandler);
        this.onGet();
        this.showMessage(`Menu item has been updated.`);
    }
}

const main = async () => {
    const inventoryManagementPage = new InventoryManagementPage();
    inventoryManagementPage.mount();
}

window.addEventListener('DOMContentLoaded', main);
