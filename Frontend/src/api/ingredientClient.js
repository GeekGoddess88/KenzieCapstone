import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class IngredientClient extends BaseClass {

constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getIngredient', 'getAllIngredients', 'addIngredient', 'deleteIngredient', 'updateIngredient'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    clientLoaded(client) {
        this.client = client;
        if(this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async getIngredient(id, errorCallback) {
        try {
            const response = await this.client.get(`/ingredient/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getDrink", error, errorCallback)
        }
    }

    async getAllIngredients(errorCallback) {
        try {
            const response = await this.client.get(`/ingredient/all`);
            return response.data;
        } catch (error) {
            this.handleError("getIngredient", error, errorCallback)
        }
    }

    async addIngredient(name, quantity, errorCallback) {
        try {
            const response = await this.client.post(`/ingredient`, {
                "name" : name,
                "quantity" : quantity
            });
            return response.data;
        } catch (error) {
            this.handleError("addDrink", error, errorCallback);
        }
    }

    async updateIngredient(id, name, quantity, errorCallback) {

        try {
            const response = await this.client.put(`/ingredient/${id}`, {
                "id" : id,
                "name" : name,
                "quantity" : quantity
            });
        } catch (error) {
            this.handleError("addDrink", error, errorCallback);
        }
    }

    async deleteIngredient(id, errorCallback) {
        try {
            const response = await this.client.delete(`/ingredient/${id}`, {
                "id" : id
            });
            return response.data;
        } catch (error) {
            this.handleError("deleteIngredient", error, errorCallback);
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