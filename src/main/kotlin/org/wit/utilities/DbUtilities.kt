package org.wit.utilities

import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.Users
import org.wit.domain.UserDTO

fun mapToUserDTO(it: ResultRow) = UserDTO(
    id = it[Users.id],
    fname = it[Users.fname],
    lname = it[Users.lname],
    email = it[Users.email],
    password = it[Users.password],
    weight = it[Users.weight],
    height = it[Users.height],
    gender = it[Users.gender],
    age = it[Users.age]
)