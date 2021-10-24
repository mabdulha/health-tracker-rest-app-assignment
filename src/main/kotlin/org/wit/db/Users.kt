package org.wit.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one user.
//       Database wise, this is the table object.

object Users : Table("users") {
    val id = integer("id").autoIncrement().primaryKey()
    val fname = varchar("fname", 50)
    val lname = varchar("lname", 50)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val weight = double("weight")
    val height = float("height")
    val gender = char("gender")
    val age = integer("age")
}