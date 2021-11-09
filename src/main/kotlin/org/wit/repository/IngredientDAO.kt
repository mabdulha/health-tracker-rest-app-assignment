package org.wit.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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

    //Find a specific ingredient by ingredient id
    fun findByIngredientId(id: Int): IngredientDTO? {
        return transaction {
            Ingredients
                .select { Ingredients.id eq id}
                .map{ mapToIngredientDTO(it) }
                .firstOrNull()
        }
    }

    //Save a meal in the database
    fun save(ingredientDTO: IngredientDTO): Int? {
        return transaction {
            Ingredients.insert {
                it[name] = ingredientDTO.name
                it[energy] = ingredientDTO.energy
                it[protein] = ingredientDTO.protein
                it[carbs] = ingredientDTO.fat
                it[fat] = ingredientDTO.carbs
                it[sodium] = ingredientDTO.sodium
            } get Ingredients.id
        }
    }

}