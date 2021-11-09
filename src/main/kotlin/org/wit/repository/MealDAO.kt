package org.wit.repository

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
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

}