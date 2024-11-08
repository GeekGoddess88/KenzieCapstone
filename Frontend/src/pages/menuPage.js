import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import drinkIngredientModule from "../api/drinkIngredientModule";

class MenuPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'renderMenu'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        await this.onGet();
        this.dataStore.addChangeListener(this.renderMenu);
    }

    async onGet() {
        try {
            const drinks = await drinkIngredientModule.getAllDrinks();
            this.dataStore.set("drinks", drinks);
        } catch (error) {
            console.error("Failed to load drinks into menu:", error);
        }
    }

    renderMenu() {
        const drinks = this.dataStore.get("drinks/");
        let renderArea = document.getElementById("menu-content");

        if (drinks && drinks.length > 0) {
            renderArea.innerHtml = drinks.map(drink => `<p>${drink.name}</p>`).join('');
        } else {
            renderArea.innerHTML = "<p>No drinks available on the menu to display.</p>";
        }
    }

}
const main = async () => {
    const menuPage = new MenuPage();
    await menuPage.mount();
}
window.addEventListener('DOMContentLoaded', main);
