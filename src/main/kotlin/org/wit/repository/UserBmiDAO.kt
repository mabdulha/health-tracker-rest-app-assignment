package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Exercises
import org.wit.db.UserBmi
import org.wit.domain.ExerciseDTO
import org.wit.domain.UserBmiDTO
import org.wit.utilities.mapToExerciseDTO
import org.wit.utilities.mapToUserBmiDTO

class UserBmiDAO {

    //Get all the bmis in the database regardless of user id
    fun getAll(): ArrayList<UserBmiDTO> {
        val userBmiList: ArrayList<UserBmiDTO> = arrayListOf()
        transaction {
            UserBmi.selectAll().map {
                userBmiList.add(mapToUserBmiDTO(it)) }
        }
        return userBmiList
    }

    fun findBmiScoresByUserId (id: Int): ArrayList<ResultRow> {
        val bmiList: ArrayList<ResultRow> = arrayListOf()
        transaction {
            UserBmi.slice(UserBmi.bmi)
                .select { UserBmi.userId eq id }
                .map { bmiList.add(it) }
        }
        return bmiList
    }

    fun save (userBmiDTO: UserBmiDTO): Int? {
        return transaction {
            UserBmi.insert {
                it[bmi] = userBmiDTO.bmi
                it[userId] = userBmiDTO.userId
            } get UserBmi.id
        }
    }

}