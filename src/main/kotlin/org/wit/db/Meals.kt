package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one meal.
//       Database wise, this is the table object.

object Meals: Table("meals") {
    val id = integer("id").autoIncrement().primaryKey()
    val image = varchar("image", 255).nullable()
    val name = varchar("name", 100).nullable()
    val energy = integer("energy").default(0).nullable()
    val calories = integer("calories").default(0).nullable()
    val protein = double("protein").default(0.00).nullable()
    val fat = double("fat").default(0.00).nullable()
    val carbs = double("carbs").default(0.00).nullable()
    var sodium = double("sodium").default(0.00).nullable()
    val loves = integer("loves").default(0).nullable()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE).nullable()
}