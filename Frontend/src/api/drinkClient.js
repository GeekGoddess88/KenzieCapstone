import axios from 'axios';
import BaseClass from "../util/baseClass"

export default class DrinkClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getDrink', 'getAllDrinks', 'addDrink', 'deleteDrink', 'updateDrink'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    async getDrink(id, errorCallback) {
        try {
            const response = await this.client.get(`/drink/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getDrink", error, errorCallback)
        }
    }

    async getAllDrinks(errorCallback) {
        try {
            const response = await this.client.get(`/drink/all`);
            return response.data;
        } catch (error) {
            this.handleError("getDrink", error, errorCallback)
        }
    }

    async addDrink(name, ingredients, recipe, errorCallback) {
        try {
            const response = await this.client.post(`/drink`, {
                "name": name,
                "ingredients": ingredients,
                "recipe": recipe
            });
            return response.data;
        } catch (error) {
            this.handleError("addDrink", error, errorCallback);
        }
    }

    async updateDrink(id, name, ingredients, recipe, errorCallback) {

        try {
            const response = await this.client.put(`/drink/${id}`, {
                "id": id,
                "name": name,
                "ingredients": ingredients,
                "recipe": recipe
            });
        } catch (error) {
            this.handleError("addDrink", error, errorCallback);
        }
    }

    async deleteDrink(id, errorCallback) {
        try {
            const response = await this.client.delete(`/drink/${id}`, {
                "id": id
            });
            return response.data;
        } catch (error) {
            this.handleError("deleteDrink", error, errorCallback);
        }
    }

    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error/*.message*/.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
