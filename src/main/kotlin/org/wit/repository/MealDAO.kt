package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Exercises
import org.wit.db.Meals
import org.wit.domain.MealDTO
import org.wit.utilities.mapToMealDTO

class MealDAO {

    //Get all the meals in the database
    fun getAll(): ArrayList<MealDTO> {
        val exercisesList: ArrayList<MealDTO> = arrayListOf()
        transaction {
            Meals.selectAll().map {
                exercisesList.add(mapToMealDTO(it)) }
        }
        return exercisesList
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

    //Save a meal in the database
    fun save(mealDTO: MealDTO): Int? {
        return transaction {
            Meals.insert {
                it[name] = mealDTO.name
                it[calories] = mealDTO.calories
                it[protein] = mealDTO.protein
                it[fat] = mealDTO.fat
                it[carbs] = mealDTO.carbs
            } get Meals.id
        }
    }

    //Update a meal by passing in the meal id
    fun updateByMealId(mealId: Int, mealDTO: MealDTO): Int {
        return transaction {
            Meals.update ({
                Meals.id eq mealId}) {
                if (mealDTO.name != null)
                    it[name] = mealDTO.name
                if (mealDTO.calories != null)
                    it[calories] = mealDTO.calories
                if (mealDTO.protein != null)
                    it[protein] = mealDTO.protein
                if (mealDTO.fat != null)
                    it[fat] = mealDTO.fat
                if (mealDTO.carbs != null)
                    it[carbs] = mealDTO.carbs

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