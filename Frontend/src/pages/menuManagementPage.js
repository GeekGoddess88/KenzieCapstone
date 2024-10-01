import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import DrinkClient from "../api/drinkClient";
import IngredientClient from "../api/ingredientClient";

class MenuManagementPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onGet', 'onGetIngredients', 'onDelete', 'onUpdate', 'onSelectIngredient',
        'renderDrinkList', 'renderCreateIngredientList', 'renderUpdateIngredientList'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('create-drink-form').addEventListener('submit', this.onCreate);
        document.getElementById('update-drink-form').addEventListener('submit', this.onUpdate);
        document.getElementById('remove-drink-form').addEventListener('submit', this.onDelete);
        this.drinkClient = new DrinkClient();
        this.ingredientClient = new IngredientClient();



        this.onGet();
        this.dataStore.addChangeListener(this.renderDrinkList);
        this.dataStore.addChangeListener(this.renderCreateIngredientList);
        this.dataStore.addChangeListener(this.renderUpdateIngredientList);
    }

    // Render method

    async renderDrinkList() {
        const drinks = this.dataStore.get("drink");
        let renderArea = document.getElementById("remove-drink-form");


        if (drinks.length > 0) {
            renderArea = document.getElementById("drinks-in-menu");
            let myHtml = "<select id=delete-options>"
            for (let drink of drinks) {
                myHtml += "<option value=" + drink.id + ">" + drink.name + "</option>"
            }
            myHtml += "</select><p></p><button type=submit class=form-button>Remove</button>";

            renderArea.innerHTML = myHtml;
        } else {
            renderArea.innerHTML = "No Drinks in Menu";
        }
    }

    async renderCreateIngredientList() {
        const ingredients = await this.ingredientClient.getAllIngredients(this.errorHandler);
        let renderArea = document.getElementById("ingredients-in-create-form");


        if (ingredients.length > 0) {
            renderArea = document.getElementById("ingredients-in-create-form");
            let myHtml = ""
            let checkId = 1;
            for (let i of ingredients) {
                myHtml += "<label for=ingredient-create-choice-" + checkId +
                "><input type=checkbox id=ingredient-create-choice-" +
                checkId +  " value=" + i.id + ">" + i.name + "</label>";
                checkId++;
            }
            renderArea.innerHTML = myHtml;
        } else {
            renderArea.innerHTML = "No ingredients to select";
        }
    }

    async renderUpdateIngredientList() {
        const ingredients = await this.ingredientClient.getAllIngredients(this.errorHandler);
        let renderArea = document.getElementById("ingredients-in-update-form");


        if (ingredients.length > 0) {
            renderArea = document.getElementById("ingredients-in-update-form");
            let myHtml = ""
            let checkId = 1;
            for (let i of ingredients) {
                myHtml += "<label for=ingredient-update-choice-" + checkId +
                "><input type=checkbox id=ingredient-update-choice-" +
                checkId +  " value=" + i.id + ">" + i.name + "</label>";
                checkId++;
            }
            renderArea.innerHTML = myHtml;
        } else {
            renderArea.innerHTML = "No ingredients to select";
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
        let drinkToDelete = await this.drinkClient.getDrink(selectedValue, this.errorHandler);
        await this.drinkClient.deleteDrink(selectedValue, this.errorHandler());
        this.showMessage(`${drinkToDelete.name} has been removed.`)

//        this.onGet();
    }

    async onGet(event) {
        let result = await this.drinkClient.getAllDrinks(this.errorHandler);
        this.dataStore.set("drink", result);
    }

    async onGetIngredients(event) {
        let result = await this.ingredientClient.getAllIngredients(this.errorHandler);
        this.dataStore.set("ingredient", result);
    }



    async onCreate(event) {
        event.preventDefault();
        let backendIngredientList = await this.ingredientClient.getAllIngredients(this.errorHandler);

        let name = document.getElementById("create-drink-name").value;

        let ingredientSelections = [];
        let loop = true;
        let i = 1;
        while (loop) {
            let selection = document.querySelector("#ingredient-create-choice-" + i);
            if (selection) {
                if (selection.checked) {
                    ingredientSelections.push(document.getElementById("ingredient-create-choice-" + i).value);
                }
                i++;
            } else {
                 loop = false;
            }
        }

        let ingredientsArray = [];
        for (let i of ingredientSelections) {
            let ingredient = await this.ingredientClient.getIngredient(i, this.errorHandler);
            ingredientsArray.push(ingredient);
        }

        let recipe = document.getElementById("create-drink-recipe").value;

        const createdDrink = await this.drinkClient.addDrink(name, ingredientsArray, recipe, this.errorHandler);

        console.log(createdDrink.id)
        console.log(createdDrink.name)
        console.log(createdDrink.ingredients)
        console.log(createdDrink.recipe)

        if (createdDrink) {
            this.showMessage(`${createdDrink.name} has been added to the menu.`)
        } else {
            this.errorHandler("Drink creation error");
        }
        this.onGet();
//        this.renderDrinkList();
        window.location.href = window.location.href;
    }

    async onUpdate(event) {
        event.preventDefault();
        let backendIngredientList = await this.ingredientClient.getAllIngredients(this.errorHandler);

        let name = document.getElementById("update-drink-name").value;

        let ingredientSelections = [];
        let loop = true;
        let i = 1;
        while (loop) {
            let selection = document.querySelector("#ingredient-update-choice-" + i);
            if (selection) {
                if (selection.checked) {
                    ingredientSelections.push(document.getElementById("ingredient-update-choice-" + i).value);
                }
                i++;
            } else {
                 loop = false;
            }
        }

        let ingredientsArray = [];
        for (let i of ingredientSelections) {
            let ingredient = await this.ingredientClient.getIngredient(i, this.errorHandler);
            ingredientsArray.push(ingredient);
        }

        let recipe = document.getElementById("update-drink-recipe").value;

        let allDrinks = await this.drinkClient.getAllDrinks(this.errorHandler);
        let id = "";

        for (let drink of allDrinks) {
            if (drink.name == name) {
                id = drink.id;
            }
        }
        if (id == "") {
            this.errorHandler("Drink update error");
            this.showMessage(`${name} does not exist on the menu.`);
        } else {
            const updatedDrink = await this.drinkClient.updateDrink(id, name, ingredientsArray, recipe, this.errorHandler);
            this.onGet();
            this.showMessage(`Menu item has been updated.`)
        }
    }
}

const main = async () => {
    const menuManagementPage = new MenuManagementPage();
    menuManagementPage.mount();
}

window.addEventListener('DOMContentLoaded', main);