package org.wit.repository

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Exercises
import org.wit.domain.ExerciseDTO
import org.wit.utilities.mapToExerciseDTO

class ExerciseDAO {

    //Get all the exercises in the database regardless of user id
    fun getAll(): ArrayList<ExerciseDTO> {
        val exercisesList: ArrayList<ExerciseDTO> = arrayListOf()
        transaction {
            Exercises.selectAll().map {
                exercisesList.add(mapToExerciseDTO(it)) }
        }
        return exercisesList
    }

}