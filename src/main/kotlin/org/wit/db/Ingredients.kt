package org.wit.db

import org.jetbrains.exposed.sql.Table

object Ingredients: Table("ingredients") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100).nullable()
    val energy = integer("energy").nullable()
    var protein = double("protein").nullable()
    var carbs = double("carbs").nullable()
    var fat = double("fat").nullable()
    var sodium = double("sodium").nullable()
}