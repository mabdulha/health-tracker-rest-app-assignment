package org.wit.repository

import org.wit.domain.UserDTO

class UserDAO {

    private val users = arrayListOf(
        UserDTO(fname = "Alice", lname = "Cullen", email = "alice@cullen.com", password = "secret", weight = 2.0, height = 1.555f, gender = 'M', age = 25, id = 0),
        UserDTO(fname = "Bob", lname = "Cat", email = "bob@cat.ie", password = "secret123", weight = 2.0, height = 1.555f, gender = 'F', age = 18, id = 1),
        UserDTO(fname = "Mary", lname = "Contrary", email = "mary@contrary.com", password = "secretpass", weight = 2.0, height = 1.555f, gender = 'F', age = 55, id = 2),
        UserDTO(fname = "Carol", lname = "Singer", email = "carol@singer.com", password = "secretsecret", weight = 2.0, height = 1.555f, gender = 'M', age = 32, id = 3)
    )

    fun getAll() : ArrayList<UserDTO>{
        return users
    }

    fun findById(id: Int): UserDTO?{
        return users.find {it.id == id}
    }

    fun save(userDTO: UserDTO){
        users.add(userDTO)
    }

    fun findByEmail(email: String) :UserDTO?{
        return users.find { it.email == email }
    }

    fun delete(id: Int) {
        val user = findById(id)
        users.remove(user)
    }

    fun update(id: Int, userDTO: UserDTO){
        val user = findById(id)
        user?.fname = userDTO.fname
        user?.lname = userDTO.lname
        user?.email = userDTO.email
        user?.password = userDTO.password
        user?.weight = userDTO.weight
        user?.height = userDTO.height
        user?.gender = userDTO.gender
        user?.age = userDTO.age
    }

}