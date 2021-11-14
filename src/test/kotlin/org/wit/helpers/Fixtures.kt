package org.wit.helpers

import org.jetbrains.exposed.sql.SchemaUtils
import org.wit.db.Exercises
import org.wit.db.Users
import org.wit.domain.ExerciseDTO
import org.wit.domain.UserDTO
import org.wit.repository.*

//Users
const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
const val validFName = "Michael"
const val validLName = "Hogan"
const val validEmail = "testuser@test.com"
const val validPassword = "secret"
const val validWeight = 65.2
const val validHeight = 164f
const val validAge = 25
const val validGender = 'M'

//Exercises
const val updatedName = "Calf Raises"
const val updatedDescription = "Calf raises are a method of exercising the gastrocnemius, tibialis posterior, peroneals and soleus muscles of the lower leg."
const val updatedCalories = 65
const val updatedDuration = 25.00
const val updatedMuscle = "Calf"

val users = arrayListOf(
    UserDTO(fname = "Alice", lname = "Cullen", email = "alice@cullen.com", password = "secret", weight = 2.0, height = 170f, gender = 'F', age = 25, id = 1),
    UserDTO(fname = "Bob", lname = "Cat", email = "bob@cat.ie", password = "secret123", weight = 2.0, height = 1.555f, gender = 'F', age = 18, id = 2),
    UserDTO(fname = "Mary", lname = "Contrary", email = "mary@contrary.com", password = "secret", weight = 2.0, height = 1.555f, gender = 'F', age = 55, id = 3),
    UserDTO(fname = "Carol", lname = "Singer", email = "carol@singer.com", password = "secret789", weight = 2.0, height = 1.555f, gender = 'M', age = 32, id = 4)
)

val exercises = arrayListOf(
    ExerciseDTO(name = "Shoulder Press", description = "Exercise which targets the shoulder muscle", calories = 70, duration = 15.00, muscle = "Shoulder", views = 25, id = 1, userId = 1, image = ""),
    ExerciseDTO(name = "Machine Fly", description = "The fly machine is ideal for increasing chest strength and muscle mass by targeting the pectoralis muscles", calories = 40, duration = 13.00, muscle = "Chest", views = 16, id = 2, userId = 1, image = ""),
    ExerciseDTO(name = "Bench Press", description = "Bench presses are an exercise that can be used to tone the muscles of the upper body", calories = 100, duration = 30.00, muscle = "Shoulder", views = 14, id = 3, userId = 2, image = ""),
    ExerciseDTO(name = "Shoulder Press", description = "Exercise which targets the shoulder muscle", calories = 70, duration = 15.00, muscle = "Shoulder", views = 7, id = 4, userId = 3, image = "")
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(user1)
    userDAO.save(user2)
    userDAO.save(user3)
    return userDAO
}

fun populateExerciseTable(): ExerciseDAO {
    SchemaUtils.create(Exercises)
    val exerciseDAO = ExerciseDAO()
    exerciseDAO.save(exercise1)
    exerciseDAO.save(exercise2)
    exerciseDAO.save(exercise3)
    return exerciseDAO
}
