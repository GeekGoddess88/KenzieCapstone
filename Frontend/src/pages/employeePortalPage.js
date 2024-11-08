import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
class EmployeePortalPage extends BaseClass {

    constructor() {
        super();
        this.bindMethods(['redirectToMenuManagement', 'redirectToIngredientManagement', 'redirectToBaristaMenu']);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('menu-button').addEventListener('click', this.redirectToMenuManagement);
        document.getElementById('inventory-button').addEventListener('click', this.redirectToIngredientManagement);
        document.getElementById('barista-button').addChangeListener('click'.this.redirectToBaristaMenu);
    }

    redirectToMenuManagement() {
        window.location.href = "../menu-management.html";
    }

    redirectToIngredientManagement() {
        window.location.href = "../inventory-management.html";
    }

    redirectToBaristaMenu() {
        window.location.href = "../barista-menu.html"
    }
}

const main = async () => {
    const employeePortalPage = new EmployeePortalPage();
    await employeePortalPage.mount();
}
window.addEventListener('DOMContentLoaded', main);
