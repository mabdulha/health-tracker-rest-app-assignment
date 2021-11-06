package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Exercises: Table("exercises") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50).nullable()
    val description = varchar("description", 100).nullable()
    val calories = integer("calories").nullable()
    val duration = double("duration").nullable()
    val muscle = varchar("muscle", 50).nullable()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE).nullable()
}