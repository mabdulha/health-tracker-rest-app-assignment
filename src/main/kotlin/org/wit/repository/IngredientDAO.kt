package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Ingredients
import org.wit.db.MealIngredients
import org.wit.db.Meals
import org.wit.domain.IngredientDTO
import org.wit.utilities.mapToIngredientDTO

class IngredientDAO {

    //Get all the ingredients in the database
    fun getAll(): ArrayList<IngredientDTO> {
        val ingredientList: ArrayList<IngredientDTO> = arrayListOf()
        transaction {
            Ingredients.selectAll().map {
                ingredientList.add(mapToIngredientDTO(it)) }
        }
        return ingredientList
    }

    //Find a specific ingredient by ingredient id
    fun findByIngredientId(id: Int): IngredientDTO? {
        return transaction {
            Ingredients
                .select { Ingredients.id eq id}
                .map{ mapToIngredientDTO(it) }
                .firstOrNull()
        }
    }

    fun findIngredientsForMeal (id: Int): ArrayList<IngredientDTO> {
        val ingredientList: ArrayList<IngredientDTO> = arrayListOf()
        transaction {
            Ingredients
                .innerJoin(MealIngredients)
                .innerJoin(Meals)
                .slice(Ingredients.id, Ingredients.name, Ingredients.energy, Ingredients.calories, Ingredients.protein, Ingredients.fat, Ingredients.carbs,
                    Ingredients.sodium)
                .select { MealIngredients.mealId eq id }
                .map { ingredientList.add(mapToIngredientDTO(it)) }
        }
        return ingredientList
    }

    fun countAmountOfIngredientsInMeal (id: Int): Int {
        return transaction {
            Ingredients
                .innerJoin(MealIngredients)
                .innerJoin(Meals)
                .slice(Ingredients.id)
                .select { MealIngredients.mealId eq id }.count()
        }
    }

    fun countEnergyForMeal (id: Int): Int? {
        var sum: Int? = 0
        transaction {
            Ingredients
                .innerJoin(MealIngredients)
                .innerJoin(Meals)
                .slice(Ingredients.energy.sum())
                .select { MealIngredients.mealId eq id }
                .map { resultRow -> sum = resultRow[Ingredients.energy.sum()] }
        }
        return sum
    }

    fun countCaloriesForMeal (id: Int): Int? {
        var sum: Int? = 0
        transaction {
        Ingredients
            .innerJoin(MealIngredients)
            .innerJoin(Meals)
            .slice(Ingredients.calories.sum())
            .select { MealIngredients.mealId eq id }
            .map { resultRow -> sum = resultRow[Ingredients.calories.sum()] }
        }
        return sum
    }

    fun countProteinForMeal (id: Int): Double? {
        var sum: Double? = 0.00
        transaction {
            Ingredients
                .innerJoin(MealIngredients)
                .innerJoin(Meals)
                .slice(Ingredients.protein.sum())
                .select { MealIngredients.mealId eq id }
                .map { resultRow -> sum = resultRow[Ingredients.protein.sum()] }
        }
        return sum
    }

    fun countFatForMeal (id: Int): Double? {
        var sum: Double? = 0.00
        transaction {
            Ingredients
                .innerJoin(MealIngredients)
                .innerJoin(Meals)
                .slice(Ingredients.fat.sum())
                .select { MealIngredients.mealId eq id }
                .map { resultRow -> sum = resultRow[Ingredients.fat.sum()] }
        }
        return sum
    }

    fun countCarbsForMeal (id: Int): Double? {
        var sum: Double? = 0.00
        transaction {
            Ingredients
                .innerJoin(MealIngredients)
                .innerJoin(Meals)
                .slice(Ingredients.carbs.sum())
                .select { MealIngredients.mealId eq id }
                .map { resultRow -> sum = resultRow[Ingredients.carbs.sum()] }
        }
        return sum
    }

    fun countSodiumForMeal (id: Int): Double? {
        var sum: Double? = 0.00
        transaction {
            Ingredients
                .innerJoin(MealIngredients)
                .innerJoin(Meals)
                .slice(Ingredients.sodium.sum())
                .select { MealIngredients.mealId eq id }
                .map { resultRow -> sum = resultRow[Ingredients.sodium.sum()] }
        }
        return sum
    }

    //Save an ingredient in the database
    fun save(ingredientDTO: IngredientDTO): Int? {
        return transaction {
            Ingredients.insert {
                it[name] = ingredientDTO.name
                it[energy] = ingredientDTO.energy
                it[calories] = ingredientDTO.calories
                it[protein] = ingredientDTO.protein
                it[fat] = ingredientDTO.fat
                it[carbs] = ingredientDTO.carbs
                it[sodium] = ingredientDTO.sodium
            } get Ingredients.id
        }
    }

    //Update an ingredient by passing in the ingredient id
    fun updateByIngredientId(ingredientId: Int, ingredientDTO: IngredientDTO): Int {
        return transaction {
            Ingredients.update ({
                Ingredients.id eq ingredientId}) {
                if (ingredientDTO.name != null)
                    it[name] = ingredientDTO.name
                if (ingredientDTO.energy != null)
                    it[energy] = ingredientDTO.energy
                if (ingredientDTO.calories != null)
                    it[calories] = ingredientDTO.calories
                if (ingredientDTO.protein != null)
                    it[protein] = ingredientDTO.protein
                if (ingredientDTO.fat != null)
                    it[fat] = ingredientDTO.fat
                if (ingredientDTO.carbs != null)
                    it[carbs] = ingredientDTO.carbs
                if (ingredientDTO.sodium != null)
                    it[sodium] = ingredientDTO.sodium
            }
        }
    }

    //Delete ingredient by passing the ingredient id
    fun deleteByIngredientId (ingredientId: Int): Int {
        return transaction{
            Ingredients.deleteWhere { Ingredients.id eq ingredientId }
        }
    }

}