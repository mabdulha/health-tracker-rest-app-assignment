package org.wit.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one user.
//       Database wise, this is the table object.

object Users : Table("users") {
    val id = integer("id").autoIncrement().primaryKey()
    val fname = varchar("fname", 50).nullable()
    val lname = varchar("lname", 50).nullable()
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val weight = double("weight").nullable()
    val height = float("height").nullable()
    val gender = char("gender").nullable()
    val age = integer("age").nullable()
}