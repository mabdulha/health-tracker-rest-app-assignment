package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one exercise.
//       Database wise, this is the table object.

object Exercises: Table("exercises") {
    val id = integer("id").autoIncrement().primaryKey()
    val image = varchar("image", 255).nullable()
    val name = varchar("name", 50).nullable()
    val description = varchar("description", 255).nullable()
    val calories = integer("calories").nullable()
    val duration = double("duration").nullable()
    val muscle = varchar("muscle", 50).nullable()
    val views = integer("views").default(0).nullable()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE).nullable()
}