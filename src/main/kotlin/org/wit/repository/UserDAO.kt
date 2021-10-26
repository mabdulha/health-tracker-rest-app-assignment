package org.wit.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import org.wit.db.Users
import org.wit.domain.UserDTO
import org.wit.utilities.mapToUserDTO

class UserDAO {

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
        val hashed = BCrypt.hashpw(userDTO.password, BCrypt.gensalt())
        transaction {
            Users.insert {
                it[fname] = userDTO.fname
                it[lname] = userDTO.lname
                it[email] = userDTO.email
                it[password] = hashed
                it[weight] = userDTO.weight
                it[height] = userDTO.height
                it[gender] = userDTO.gender
                it[age] = userDTO.age
            }
        }
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