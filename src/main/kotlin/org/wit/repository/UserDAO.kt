package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Users
import org.wit.domain.UserDTO
import org.wit.utilities.hashPassword
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

    fun findById(id: Int): UserDTO? {
        return transaction {
            Users.select {
                Users.id eq id}
                .map{mapToUserDTO(it)}
                .firstOrNull()
        }
    }

    fun save(userDTO: UserDTO) : Int? {
        return transaction {
            Users.insert {
                it[avatar] = userDTO.avatar
                it[fname] = userDTO.fname
                it[lname] = userDTO.lname
                it[email] = userDTO.email
                it[password] = hashPassword(userDTO.password)
                it[weight] = userDTO.weight
                it[height] = userDTO.height
                it[gender] = userDTO.gender
                it[age] = userDTO.age
            } get Users.id
        }
    }

    fun findByEmail(email: String?): UserDTO? {
        return transaction {
            Users.select {
                Users.email eq email
            }
                .map { mapToUserDTO(it) }
                .firstOrNull()
        }
    }

    fun delete(id: Int): Int {
        return transaction{ Users.deleteWhere{
            Users.id eq id
        }
        }
    }

    fun update(id: Int, userDTO: UserDTO) : Int {
        return transaction {
            Users.update ({
                Users.id eq id}) {
                if (userDTO.avatar != null)
                    it[avatar] = userDTO.avatar
                if (userDTO.fname != null)
                    it[fname] = userDTO.fname
                if (userDTO.lname != null)
                    it[lname] = userDTO.lname
                if (userDTO.email != null)
                    it[email] = userDTO.email
                if (userDTO.password != null)
                    it[password] = hashPassword(userDTO.password)
                if (userDTO.weight != null)
                    it[weight] = userDTO.weight
                if (userDTO.height != null)
                    it[height] = userDTO.height
                if (userDTO.gender != null)
                    it[gender] = userDTO.gender
                if (userDTO.age != null)
                    it[age] = userDTO.age
            }
        }
    }

}