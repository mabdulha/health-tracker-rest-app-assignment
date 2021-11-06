package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Exercises: Table("exercises") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
    val description = varchar("description", 100)
    val calories = integer("calories")
    val duration = double("duration")
    val muscle = varchar("muscle", 50)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}