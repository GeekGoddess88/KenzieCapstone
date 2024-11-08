import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import drinkIngredientModule from "../api/drinkIngredientModule";

class InventoryManagementPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onGet', 'onDelete', 'onUpdate',
            'renderIngredientList'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('create-ingredient-form').addEventListener('submit', this.onCreate);
        document.getElementById('update-ingredient-form').addEventListener('submit', this.onUpdate);
        document.getElementById('remove-ingredient-form').addEventListener('submit', this.onDelete);


        await this.onGet();
        this.dataStore.addChangeListener(this.renderIngredientList);
    }

    async onGet() {
        try {
            const ingredients = await drinkIngredientModule.getAllIngredients();
            this.dataStore.set("ingredients", ingredients);
        } catch (error) {
            console.error("Failed to load ingredients:", error);
        }
    }

    async onCreate(event) {
        event.preventDefault();
        const name = document.getElementById("create-ingredient-name").value;
        const quantity = document.getElementById("create-ingredient-quantity").value;
        try {
            const createdIngredient = await drinkIngredientModule.addIngredient(name, quantity);
            this.showMessage(`${createdIngredient.name} has been added to the inventory.`)
            await this.onGet();
        } catch (error) {
            console.error("Error adding ingredient:", error);
        }
    }

    async onUpdate(event) {
        event.preventDefault();
        const selectedIngredientId = document.getElementById('ingredient-update-options').value;
        const quantity = document.getElementById("update-ingredient-quantity").value;

        try {
            const ingredientToUpdate = await drinkIngredientModule.getIngredientById(selectedIngredientId);
            const updatedIngredient = await drinkIngredientModule.updateIngredient(selectedIngredientId, {
                name: ingredientToUpdate.name,
                quantity
            });
            this.showMessage(`Ingredient updated: ${updatedIngredient.name}.`);
            await this.onGet();
        } catch (error) {
            console.error("Error updating ingredients:", error);
        }
    }

    async onDelete(event) {
        event.preventDefault();
        const selectedId = document.getElementById('delete-options').value;
        try {
            await drinkIngredientModule.deleteIngredient(selectedId);
            this.showMessage("Ingredient removed from inventory.");
            await this.onGet();
        } catch (error) {
            console.error("Error deleting ingredient:", error);
        }
    }


    // Render method

    async renderIngredientList() {
        const ingredients = this.dataStore.get("ingredients");
        const deleteSelect = document.getElementById("delete-options");
        const updateSelect = document.getElementById("ingredient-update-options");

        if (ingredients && ingredients.length > 0) {
            deleteSelect.innerHTML = ingredients.map(ingredient => `<option value="${ingredient.id}">${ingredient.name}</option>`).join("");
            updateSelect.innerHTML = ingredients.map(ingredient => `<option value="${ingredient.id}">${ingredient.name}</option>`).join("");
        } else {
            deleteSelect.innerHTML = "<option>No Ingredients Available</option>";
            updateSelect.innerHTML = "<option>No Ingredients Available</option>";
        }
    }
}

const main = async () => {
    const inventoryManagementPage = new InventoryManagementPage();
    await inventoryManagementPage.mount();
}

window.addEventListener('DOMContentLoaded', main);
