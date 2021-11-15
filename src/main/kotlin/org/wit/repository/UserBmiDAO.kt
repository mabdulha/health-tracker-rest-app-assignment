package org.wit.repository

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.UserBmi

class UserBmiDAO {

    fun findBmiScoresByUserId (id: Int): ArrayList<ResultRow> {
        val bmiList: ArrayList<ResultRow> = arrayListOf()
        transaction {
            UserBmi.slice(UserBmi.bmi)
                .select { UserBmi.userId eq id }
                .map { bmiList.add(it) }
        }
        return bmiList
    }

}