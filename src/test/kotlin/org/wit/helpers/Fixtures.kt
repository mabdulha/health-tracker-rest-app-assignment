package org.wit.helpers

import org.jetbrains.exposed.sql.SchemaUtils
import org.wit.db.Users
import org.wit.domain.UserDTO
import org.wit.repository.UserDAO
import org.wit.repository.user1
import org.wit.repository.user2
import org.wit.repository.user3

const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
const val validFName = "Michael"
const val validLName = "Hogan"
const val validEmail = "testuser@test.com"
const val validPassword = "secret"
const val validWeight = 65.2
const val validHeight = 164f
const val validAge = 25
const val validGender = 'M'

val users = arrayListOf(
    UserDTO(fname = "Alice", lname = "Cullen", email = "alice@cullen.com", password = "secret", weight = 2.0, height = 170f, gender = 'F', age = 25, id = 1),
    UserDTO(fname = "Bob", lname = "Cat", email = "bob@cat.ie", password = "secret123", weight = 2.0, height = 1.555f, gender = 'F', age = 18, id = 2),
    UserDTO(fname = "Mary", lname = "Contrary", email = "mary@contrary.com", password = "secret", weight = 2.0, height = 1.555f, gender = 'F', age = 55, id = 3),
    UserDTO(fname = "Carol", lname = "Singer", email = "carol@singer.com", password = "secret789", weight = 2.0, height = 1.555f, gender = 'M', age = 32, id = 4)
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(user1)
    userDAO.save(user2)
    userDAO.save(user3)
    return userDAO
}
