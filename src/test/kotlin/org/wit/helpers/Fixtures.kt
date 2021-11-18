package org.wit.helpers

import org.jetbrains.exposed.sql.SchemaUtils
import org.wit.db.*
import org.wit.domain.*
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
    UserDTO(fname = "Alice", lname = "Cullen", email = "alice@cullen.com", password = "secret", weight = 2.0, height = 170f, gender = 'F', age = 25, id = 1, avatar = ""),
    UserDTO(fname = "Bob", lname = "Cat", email = "bob@cat.ie", password = "secret123", weight = 2.0, height = 1.555f, gender = 'F', age = 18, id = 2, avatar = ""),
    UserDTO(fname = "Mary", lname = "Contrary", email = "mary@contrary.com", password = "secret", weight = 2.0, height = 1.555f, gender = 'F', age = 55, id = 3, avatar = ""),
    UserDTO(fname = "Carol", lname = "Singer", email = "carol@singer.com", password = "secret789", weight = 2.0, height = 1.555f, gender = 'M', age = 32, id = 4, avatar = "")
)

val exercises = arrayListOf(
    ExerciseDTO(name = "Shoulder Press", description = "Exercise which targets the shoulder muscle", calories = 70, duration = 15.00, muscle = "Shoulder", views = 25, id = 1, userId = 1, image = ""),
    ExerciseDTO(name = "Machine Fly", description = "The fly machine is ideal for increasing chest strength and muscle mass by targeting the pectoralis muscles", calories = 40, duration = 13.00, muscle = "Chest", views = 16, id = 2, userId = 1, image = ""),
    ExerciseDTO(name = "Bench Press", description = "Bench presses are an exercise that can be used to tone the muscles of the upper body", calories = 100, duration = 30.00, muscle = "Shoulder", views = 14, id = 3, userId = 2, image = ""),
    ExerciseDTO(name = "Shoulder Press", description = "Exercise which targets the shoulder muscle", calories = 70, duration = 15.00, muscle = "Shoulder", views = 7, id = 4, userId = 3, image = "")
)

val ingredients = arrayListOf(
    IngredientDTO(id = 1, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main1.png", name = "Lamb", energy = 5, calories = 55, protein = 4.44, fat = 4.44, carbs = 4.44, sodium = 4.44),
    IngredientDTO(id = 2, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main2.png", name = "Chicken Drumstick", energy = 1, calories = 48, protein = 5.55, fat = 5.55, carbs = 5.55, sodium = 5.55),
    IngredientDTO(id = 3, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main3.png", name = "Salt", energy = 35, calories = 67, protein = 6.66, fat = 6.66, carbs = 6.66, sodium = 6.66),
    IngredientDTO(id = 4, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main4.png", name = "Carrots", energy = 46, calories = 124, protein = 7.77, fat = 7.77, carbs = 7.77, sodium = 7.77)
)

val meals = arrayListOf(
    MealDTO(id = 1, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main1.png", name = "Cheese Cake", energy = 0, calories = 0, protein = 0.00, fat = 0.00, carbs = 0.00, sodium = 0.00, loves = 26, userId = 1),
    MealDTO(id = 2, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main2.png", name = "Beef Stew", energy = 0, calories = 0, protein = 0.00, fat = 0.00, carbs = 0.00, sodium = 0.00, loves = 98, userId = 1),
    MealDTO(id = 3, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main3.png", name = "Chicken Salad", energy = 0, calories = 0, protein = 0.00, fat = 0.00, carbs = 0.00, sodium = 0.00, loves = 46, userId = 2),
    MealDTO(id = 4, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main4.png", name = "Veggies", energy = 0, calories = 0, protein = 0.00, fat = 0.00, carbs = 0.00, sodium = 0.00, loves = 13, userId = 3)
)

val mealIngredients = arrayListOf(
    MealIngredientDTO(id = 1, mealId = 1, ingredientId = 1),
    MealIngredientDTO(id = 2, mealId = 1, ingredientId = 2),
    MealIngredientDTO(id = 3, mealId = 2, ingredientId = 3),
    MealIngredientDTO(id = 4, mealId = 3, ingredientId = 1)
)

val userBmi = arrayListOf(
    UserBmiDTO(id = 1, bmi = 18.5, userId = 1),
    UserBmiDTO(id = 2, bmi = 22.6, userId = 1),
    UserBmiDTO(id = 3, bmi = 22.9, userId = 2),
    UserBmiDTO(id = 4, bmi = 35.4, userId = 3)
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

fun populateIngredientTable(): IngredientDAO {
    SchemaUtils.create(Ingredients)
    val ingredientDao = IngredientDAO()
    ingredientDao.save(ingredient1)
    ingredientDao.save(ingredient2)
    ingredientDao.save(ingredient3)
    return ingredientDao
}

fun populateMealTable(): MealDAO {
    SchemaUtils.create(Meals)
    val mealDAO = MealDAO()
    mealDAO.save(meal1)
    mealDAO.save(meal2)
    mealDAO.save(meal3)
    return mealDAO
}

fun populateMealIngredientTable(): MealDAO {
    SchemaUtils.create(MealIngredients)
    val mealIngredientDAO = MealDAO()
    mealIngredientDAO.saveMealAndIngredientId(mealIngredient1)
    mealIngredientDAO.saveMealAndIngredientId(mealIngredient2)
    mealIngredientDAO.saveMealAndIngredientId(mealIngredient3)
    return mealIngredientDAO
}

fun populateUserBmiTable(): UserBmiDAO {
    SchemaUtils.create(UserBmi)
    val userBmiDAO = UserBmiDAO()
    userBmiDAO.save(userBmi1)
    userBmiDAO.save(userBmi2)
    userBmiDAO.save(userBmi3)
    return userBmiDAO
}
