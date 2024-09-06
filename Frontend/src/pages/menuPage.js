import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import DrinkClient from "../api/drinkClient";

class MenuPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'renderMenu'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.drinkClient = new DrinkClient();
        this.onGet();
        this.dataStore.addChangeListener(this.renderMenu);
    }

    // Render methods

    async renderMenu() {
        const drinks = this.dataStore.get("drink");
        let renderArea = document.getElementById("menu-content");

        if (drinks) {
            let myHtml = "<ul>";
            for (let drink of drinks) {
                myHtml += "<li><h3>" + drink.name + "<h3></li>";
            }
            renderArea.innerHTML = myHtml;
        } else {
            renderArea.innerHTML = "No Menu to Display";
        }
    }


    // Event handlers

    async onGet(event) {
        let result = await this.drinkClient.getAllDrinks(this.errorHandler);
        this.dataStore.set("drink", result);
    }

}
const main = async () => {
    const menuPage = new MenuPage();
    menuPage.mount();
}
window.addEventListener('DOMContentLoaded', main);