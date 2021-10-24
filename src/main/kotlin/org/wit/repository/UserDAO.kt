package org.wit.repository

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Users
import org.wit.domain.UserDTO
import org.wit.utilities.mapToUserDTO

class UserDAO {

    private val users = arrayListOf(
        UserDTO(fname = "Alice", lname = "Cullen", email = "alice@cullen.com", password = "secret", weight = 2.0, height = 1.555f, gender = 'M', age = 25, id = 0),
        UserDTO(fname = "Bob", lname = "Cat", email = "bob@cat.ie", password = "secret123", weight = 2.0, height = 1.555f, gender = 'F', age = 18, id = 1),
        UserDTO(fname = "Mary", lname = "Contrary", email = "mary@contrary.com", password = "secretpass", weight = 2.0, height = 1.555f, gender = 'F', age = 55, id = 2),
        UserDTO(fname = "Carol", lname = "Singer", email = "carol@singer.com", password = "secretsecret", weight = 2.0, height = 1.555f, gender = 'M', age = 32, id = 3)
    )

    fun getAll(): ArrayList<UserDTO> {
        val userList: ArrayList<UserDTO> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUserDTO(it)) }
        }
        return userList
    }

    fun findById(id: Int): UserDTO?{
        return transaction {
            Users.select {
                Users.id eq id}
                .map{mapToUserDTO(it)}
                .firstOrNull()
        }
    }

    fun save(userDTO: UserDTO){
    }

    fun findByEmail(email: String) :UserDTO?{
        return transaction {
            Users.select {
                Users.email eq email
            }
                .map { mapToUserDTO(it) }
                .firstOrNull()
        }
    }

    fun delete(id: Int) {
    }

    fun update(id: Int, userDTO: UserDTO){
    }

}