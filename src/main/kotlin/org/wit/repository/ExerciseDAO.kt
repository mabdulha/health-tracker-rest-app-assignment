package org.wit.repository

import org.jetbrains.exposed.sql.*
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
    fun findByExerciseId(id: Int): ExerciseDTO? {
        return transaction {
            Exercises
                .select { Exercises.id eq id}
                .map{ mapToExerciseDTO(it)}
                .firstOrNull()
        }
    }

    //Find all exercises for a specific user id
    fun findExerciseByUserId(userId: Int): List<ExerciseDTO> {
        return transaction {
            Exercises
                .select {Exercises.userId eq userId}
                .map { mapToExerciseDTO(it) }
        }
    }

    //Save an exercise to the database
    fun save(exerciseDTO: ExerciseDTO): Int? {
        return transaction {
            Exercises.insert {
                it[name] = exerciseDTO.name
                it[description] = exerciseDTO.description
                it[calories] = exerciseDTO.calories
                it[duration] = exerciseDTO.duration
                it[views] = exerciseDTO.views
                it[muscle] = exerciseDTO.muscle
                it[userId] = exerciseDTO.userId
            } get Exercises.id
        }
    }

    //Update an exercise by passing in the exercise id
    fun updateByExerciseId(exerciseId: Int, exerciseDTO: ExerciseDTO): Int {
        return transaction {
            Exercises.update ({
                Exercises.id eq exerciseId}) {
                if (exerciseDTO.name != null)
                    it[name] = exerciseDTO.name
                if (exerciseDTO.description != null)
                    it[description] = exerciseDTO.description
                if (exerciseDTO.calories != null)
                    it[calories] = exerciseDTO.calories
                if (exerciseDTO.duration != null)
                    it[duration] = exerciseDTO.duration
                if (exerciseDTO.muscle != null)
                    it[muscle] = exerciseDTO.muscle
                if (exerciseDTO.views != null)
                    it[views] = exerciseDTO.views
                if (exerciseDTO.userId != null)
                    it[userId] = exerciseDTO.userId
            }
        }
    }

    //Delete exercise by passing the exercise id
    fun deleteByExerciseId (exerciseId: Int): Int {
        return transaction{
            Exercises.deleteWhere { Exercises.id eq exerciseId }
        }
    }

    //Delete exercise by passing the user id
    fun deleteByUserId (userId: Int): Int {
        return transaction{
            Exercises.deleteWhere { Exercises.userId eq userId }
        }
    }

}