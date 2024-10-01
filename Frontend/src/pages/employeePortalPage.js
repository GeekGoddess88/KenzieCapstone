class EmployeePortalPage {

    async mount() {
        document.getElementById('menu-button').addEventListener('button', this.redirectToMenuManagement);
    }

    async redirectToMenuManagement() {
        window.location.href = "menu-management.html";
    }
    async redirectToIngredientManagement() {
        window.location.href = "ingredients.html";
    }
}
const main = async () => {
        const employeePortalPage = new EmployeePortalPage();
        employeePortalPage.mount();
}
window.addEventListener('DOMContentLoaded', main);