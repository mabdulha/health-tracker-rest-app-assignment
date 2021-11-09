package org.wit.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one meal.
//       Database wise, this is the table object.

object Meals: Table("meals") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val calories = integer("calories").nullable()
    val protein = double("protein").nullable()
    val fat = double("fat").nullable()
    val carbs = double("carbs").nullable()
}