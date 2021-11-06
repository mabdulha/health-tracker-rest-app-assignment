package org.wit.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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

    //Find a specific exercise by exercise id
    fun findByExerciseId(id: Int): ExerciseDTO?{
        return transaction {
            Exercises
                .select() { Exercises.id eq id}
                .map{ mapToExerciseDTO(it)}
                .firstOrNull()
        }
    }

    //Find all exercises for a specific user id
    fun findExerciseByUserId(userId: Int): List<ExerciseDTO>{
        return transaction {
            Exercises
                .select {Exercises.userId eq userId}
                .map { mapToExerciseDTO(it) }
        }
    }

    //Save an exercise to the database
    fun save(exerciseDTO: ExerciseDTO){
        transaction {
            Exercises.insert {
                it[name] = exerciseDTO.name
                it[description] = exerciseDTO.description
                it[calories] = exerciseDTO.calories
                it[duration] = exerciseDTO.duration
                it[muscle] = exerciseDTO.muscle
                it[userId] = exerciseDTO.userId
            }
        }
    }

}