package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserBmi: Table("user_bmi") {
    val id = integer("id").autoIncrement().primaryKey()
    val bmi = double("bmi").nullable()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE).nullable()
}