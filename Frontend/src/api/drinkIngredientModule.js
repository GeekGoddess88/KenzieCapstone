import axios from 'axios';
const drinkIngredientModule = {

    async getAllDrinks() {
        try {
            const response = await axios.get('/drinks/all');
            return response.data;
        } catch (error) {
            console.error("Error fetching all drinks:", error);
            throw error;
        }
    },

    async getDrinkById(id) {
      try {
          const response = await axios.get(`/drinks/${id}`);
          return response.data;
      } catch (error) {
          console.error(`Error fetching drink with id ${id}:`, error);
          throw error;
      }
    },

    async addDrink(drinkData) {
        try {
            const response = await axios.post('/drinks', drinkData);
            return response.data;
        } catch (error) {
            console.error("Error adding drink:", error);
            throw error;
        }
    },

    async updateDrink(id, drinkData) {
        try {
            const response = await axios.put(`/drinks/${id}`, drinkData);
            return response.data;
        } catch (error) {
            console.error("Error updating drink:", error);
            throw error;
        }
    },

    async deleteDrink(id) {
        try {
            const response = await axios.delete(`/drinks/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error deleting drink with id ${id}:`, error);
            throw error;
        }
    },

    async getAllIngredients() {
        try {
            const response = await axios.get('/ingredients/all');
            return response.data;
        } catch (error) {
            console.error("Error fetching all ingredients:", error);
            throw error;
        }
    },

    async getIngredientById(id) {
        try {
            const response = await axios.get(`/ingredients/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching ingredient with id ${id}:`, error);
            throw error;
        }
    },

    async addIngredient(ingredientData) {
        try {
            const response = await axios.post('/ingredients', ingredientData);
            return response.data;
        } catch (error) {
            console.error("Error adding ingredient:", error);
            throw error;
        }
    },

    async updateIngredient(id, ingredientData) {
        try {
            const response = await axios.put(`/ingredients/${id}`, ingredientData);
            return response.data;
        } catch (error) {
            console.error("Error updating ingredient:", error);
            throw error;
        }
    },

    async deleteIngredient(id) {
        try {
            const response = await axios.delete(`/ingredients/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error deleting ingredient with id ${id}:`, error);
            throw error;
        }
    },
};

export default drinkIngredientModule;



