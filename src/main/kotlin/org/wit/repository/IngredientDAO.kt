package org.wit.repository

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Ingredients
import org.wit.domain.IngredientDTO
import org.wit.utilities.mapToIngredientDTO


class IngredientDAO {

    //Get all the ingredients in the database
    fun getAll(): ArrayList<IngredientDTO> {
        val exercisesList: ArrayList<IngredientDTO> = arrayListOf()
        transaction {
            Ingredients.selectAll().map {
                exercisesList.add(mapToIngredientDTO(it)) }
        }
        return exercisesList
    }

}