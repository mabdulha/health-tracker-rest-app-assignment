package org.wit.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.harium.dotenv.Env
import io.javalin.http.Context
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.wit.domain.UserDTO
import org.wit.repository.UserDAO
import org.wit.utilities.decryptPassword
import org.wit.utilities.jsonToObject
import java.util.*

object HealthTrackerAPI {

    private val userDao = UserDAO()

    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.json(users)
            ctx.status(200)
        } else {
            ctx.status(404)
            ctx.html("Error 404 - No Users Found!!")
        }

    }

    fun getUserByUserId(ctx: Context) {
        val foundId = ctx.pathParam("user-id").toInt()
        val user = userDao.findById(foundId)
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
            ctx.html("No user found with id: $foundId")
        }
    }

    fun addUser(ctx: Context) {
        val user : UserDTO = jsonToObject(ctx.body())
        if (userDao.findByEmail(user.email) == null) {
            val userId = userDao.save(user)
            if (userId != null) {
                user.id = userId
                ctx.json(user)
                ctx.status(201)
            }
        } else {
            ctx.status(404).json("The email: ${user.email}, already exists")
        }

    }

    fun getUserByEmail(ctx: Context) {
        val foundEmail = ctx.pathParam("email")
        val user = userDao.findByEmail(foundEmail)
        if (user != null) {
            ctx.status(200)
            ctx.json(user)
        } else {
            ctx.status(404)
            ctx.html("No user found with email: $foundEmail")
        }
    }

    fun deleteUser(ctx: Context){
        val foundId = ctx.pathParam("user-id").toInt()
        if (userDao.findById(foundId) != null) {
            userDao.delete(foundId)
            ctx.html("User with id: ${foundId}, deleted successfully")
            ctx.status(204)
        } else {
            ctx.status(404).json("User with id ${foundId}, does not exist")
        }
    }

    fun updateUser(ctx: Context){
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<UserDTO>(ctx.body())
        userDao.update(
            id = ctx.pathParam("user-id").toInt(),
            userDTO=user)
    }

    fun login (ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<UserDTO>(ctx.body())
        val existingUser = userDao.findByEmail(user.email)
        val secret = Base64.getDecoder().decode(Env.get("JWT_SECRET"))
        if (existingUser != null) {
            if(decryptPassword(user.password, existingUser.password)) {
                val jwt = Jwts.builder()
                    .claim("User", existingUser)
                    .signWith(Keys.hmacShaKeyFor(secret))
                    .compact()
                ctx.json(jwt)
                ctx.status(200)
            } else {
                ctx.status(401)
                ctx.json("Invalid email or password")
            }
        } else {
            ctx.status(401)
            // print(ctx.res.sendError(401, "Invalid email or password"))
            ctx.json("Invalid email or password")
        }
    }
}