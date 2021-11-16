package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Exercises
import org.wit.db.MealIngredients
import org.wit.db.Meals
import org.wit.domain.ExerciseDTO
import org.wit.domain.MealDTO
import org.wit.domain.MealIngredientDTO
import org.wit.utilities.mapToExerciseDTO
import org.wit.utilities.mapToMealDTO

class MealDAO {

    //Get all the meals in the database
    fun getAll(): ArrayList<MealDTO> {
        val mealsList: ArrayList<MealDTO> = arrayListOf()
        transaction {
            Meals.selectAll().orderBy(Meals.loves to SortOrder.DESC).map {
                mealsList.add(mapToMealDTO(it)) }
        }
        return mealsList
    }

    //Find a specific meal by meal id
    fun findByMealId(id: Int): MealDTO? {
        return transaction {
            Meals
                .select { Meals.id eq id}
                .map{ mapToMealDTO(it) }
                .firstOrNull()
        }
    }

    //Find all meals for a specific user id
    fun findMealByUserId(userId: Int): List<MealDTO> {
        return transaction {
            Meals
                .select {Meals.userId eq userId}
                .orderBy(Meals.loves to SortOrder.DESC)
                .map { mapToMealDTO(it) }
        }
    }

    //Save a meal in the database
    fun save(mealDTO: MealDTO): Int? {
        return transaction {
            Meals.insert {
                it[image] = mealDTO.image
                it[name] = mealDTO.name
                it[energy] = mealDTO.energy
                it[calories] = mealDTO.calories
                it[protein] = mealDTO.protein
                it[fat] = mealDTO.fat
                it[carbs] = mealDTO.carbs
                it[sodium] = mealDTO.sodium
                it[loves] = 0
                it[userId] = mealDTO.userId
            } get Meals.id
        }
    }

    fun saveMealAndIngredientId (mealIngredientDTO: MealIngredientDTO) {
        return transaction {
            MealIngredients.insert {
                it[mealId] = mealIngredientDTO.mealId
                it[ingredientId] = mealIngredientDTO.ingredientId
            }
        }
    }

    //Update a meal by passing in the meal id
    fun updateByMealId(mealId: Int, mealDTO: MealDTO): Int {
        return transaction {
            Meals.update ({
                Meals.id eq mealId}) {
                if (mealDTO.image != null)
                    it[image] = mealDTO.image
                if (mealDTO.name != null)
                    it[name] = mealDTO.name
                if (mealDTO.energy != null)
                    it[energy] = mealDTO.energy
                if (mealDTO.calories != null)
                    it[calories] = mealDTO.calories
                if (mealDTO.protein != null)
                    it[protein] = mealDTO.protein
                if (mealDTO.fat != null)
                    it[fat] = mealDTO.fat
                if (mealDTO.carbs != null)
                    it[carbs] = mealDTO.carbs
                if (mealDTO.sodium != null)
                    it[sodium] = mealDTO.sodium
                if (mealDTO.loves != null)
                    it[loves] = mealDTO.loves
                if (mealDTO.userId != null) {
                    it[userId] = mealDTO.userId
                }
            }
        }
    }

    //Delete meal by passing the meal id
    fun deleteByMealId (mealId: Int): Int {
        return transaction{
            Meals.deleteWhere { Meals.id eq mealId }
        }
    }

}