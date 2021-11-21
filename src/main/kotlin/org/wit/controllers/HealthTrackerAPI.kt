package org.wit.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.harium.dotenv.Env
import io.javalin.http.Context
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kotlin.math.round
import org.wit.domain.*
import org.wit.repository.*
import org.wit.utilities.decryptPassword
import org.wit.utilities.jsonToObject
import java.util.*

object HealthTrackerAPI {

    private val userDao = UserDAO()
    private val exerciseDao = ExerciseDAO()
    private val mealDao = MealDAO()
    private val ingredientDao = IngredientDAO()
    private val userBmiDAO = UserBmiDAO()

    //-------------------------------------------------------------
    // UserDAO specifics
    //-------------------------------------------------------------

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

    fun countAllUsers (ctx: Context) {
        val users = userDao.getAll().size
        if (users != 0) {
            ctx.status(200).json(users)
        } else {
            ctx.status(404).json("Error 404 - No Users Found!!")
        }
    }

    fun getUserByUserId(ctx: Context) {
        val foundId = ctx.pathParam("user-id").toInt()
        val user = userDao.findById(foundId)
        if (user != null) {
            ctx.status(200).json(user)
        } else {
            ctx.status(404).json("No user found with id: $foundId")
        }
    }

    fun addUser(ctx: Context) {
        val user : UserDTO = jsonToObject(ctx.body())
        if (userDao.findByEmail(user.email) == null) {
            val userId = userDao.save(user)
            if (userId != null) {
                user.id = userId
                ctx.status(201).json(user)
            }
        } else {
            ctx.status(409).json("The email: ${user.email}, already exists")
        }

    }

    fun getUserByEmail(ctx: Context) {
        val foundEmail = ctx.pathParam("email")
        val user = userDao.findByEmail(foundEmail)
        if (user != null) {
            ctx.status(200).json(user)
        } else {
            ctx.status(404).json("No user found with email: $foundEmail")
        }
    }

    fun deleteUser(ctx: Context){
        val foundId = ctx.pathParam("user-id").toInt()
        if (userDao.findById(foundId) != null) {
            userDao.delete(foundId)
            ctx.status(204).json("User with id: ${foundId}, deleted successfully")
        } else {
            ctx.status(404).json("User with id ${foundId}, does not exist")
        }
    }

    fun updateUser(ctx: Context){
        val foundId = ctx.pathParam("user-id").toInt()
        val user : UserDTO = jsonToObject(ctx.body())
        if ((userDao.update(id = foundId, userDTO=user)) != 0)
            ctx.status(204).json("User with id: $foundId, updated successfully")
        else
            ctx.status(404).json("User with id: $foundId, not found ")
    }

    fun login (ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<UserDTO>(ctx.body())
        val existingUser = userDao.findByEmail(user.email)
        val secret = Base64.getDecoder().decode(Env.get("JWT_SECRET") ?: System.getenv("JWT_SECRET"))
        if (existingUser != null) {
            if(decryptPassword(user.password, existingUser.password)) {
                val jwt = Jwts.builder()
                    .claim("User", existingUser)
                    .signWith(Keys.hmacShaKeyFor(secret))
                    .compact()
                ctx.status(200).json(jwt)
            } else {
                ctx.status(401).json("Invalid email or password")
            }
        } else {
            ctx.res.sendError(401, "Invalid email or password")
            // ctx.json("Invalid email or password")
        }
    }

    //--------------------------------------------------------------
    // ExerciseDAO specifics
    //-------------------------------------------------------------

    fun getAllExercises (ctx: Context) {
        val exercises = exerciseDao.getAll()
        if (exercises.size != 0) {
            ctx.status(200).json(exercises)
        } else {
            ctx.status(404).json("Error 404 - No Exercises Found!!")
        }
    }

    fun countAllExercises (ctx: Context) {
        val exercises = exerciseDao.getAll().size
        if (exercises != 0) {
            ctx.status(200).json(exercises)
        } else {
            ctx.status(404).json("Error 404 - No Exercises Found!!")
        }
    }

    fun getExercisesByUserId (ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val exercises = exerciseDao.findExerciseByUserId(ctx.pathParam("user-id").toInt())
            if (exercises.isNotEmpty()) {
                ctx.status(200).json(exercises)
            } else {
                ctx.status(404).json("No exercises found")
            }
        } else {
            ctx.status(404).json("No exercises associated with user")
        }
    }

    fun getExercisesByExerciseId (ctx: Context) {
        val exercise = exerciseDao.findByExerciseId(ctx.pathParam("exercise-id").toInt())
        if (exercise != null) {
            ctx.status(200).json(exercise)
        } else {
            ctx.status(404).json("No exercises found")
        }
    }

    fun addExercise (ctx: Context) {
        val exerciseDTO : ExerciseDTO = jsonToObject(ctx.body())
        val userId = exerciseDTO.userId?.let { userDao.findById(it) }
        if (userId != null) {
            val exerciseId = exerciseDao.save(exerciseDTO)
            if (exerciseId != null) {
                exerciseDTO.id = exerciseId
                ctx.status(201).json(exerciseDTO)
            }
        } else {
            ctx.status(404).json("Cannot add exercise to userid: $userId")
        }
    }

    fun updateExercise (ctx: Context) {
        val foundId = ctx.pathParam("exercise-id").toInt()
        val exercise : ExerciseDTO = jsonToObject(ctx.body())
        if (exerciseDao.updateByExerciseId(exerciseId = foundId, exerciseDTO = exercise) != 0) {
            ctx.status(204).json("Exercise with id: $foundId, updated successfully")
        } else {
            ctx.status(404).json("Could not find exercise with id: $foundId")
        }
    }

    fun incrementView (ctx: Context) {
        val foundId = ctx.pathParam("exercise-id").toInt()
        val foundExercise = exerciseDao.findByExerciseId(foundId)
        if (foundExercise != null) {
            val increment = foundExercise.views?.plus(1)
            val exercise: ExerciseDTO = jsonToObject("{\"views\":\"$increment\"}")
            if (exerciseDao.updateByExerciseId(exerciseId = foundId, exerciseDTO = exercise) != 0) {
                ctx.status(201).json("Successfully incremented view, new value = $increment")
            }
        } else {
            ctx.status(404).json("Could not find the exercise with id: $foundId")
        }
    }

    fun deleteExerciseByExerciseId (ctx: Context) {
        val foundId = ctx.pathParam("exercise-id").toInt()
        if (exerciseDao.findByExerciseId(foundId) != null) {
            exerciseDao.deleteByExerciseId(foundId)
            ctx.status(204).json("Exercise with id: $foundId, deleted successfully")
        } else {
            ctx.status(404).json("Exercise with id $foundId, does not exist")
        }
    }

    fun deleteExerciseByUserId (ctx: Context) {
        val foundId = ctx.pathParam("user-id").toInt()
        if (exerciseDao.deleteByExerciseId(foundId) != 0) {
            ctx.status(204).json("Exercises belonging to user with id: $foundId, deleted successfully")
        } else {
            ctx.status(404).json("No Exercises found for user with id: $foundId")
        }
    }

    //--------------------------------------------------------------
    // MealDAO specifics
    //-------------------------------------------------------------

    fun getAllMeals(ctx: Context) {
        val meals = mealDao.getAll()
        if (meals.size != 0) {
            ctx.status(200).json(meals)
        } else {
            ctx.status(404)
            ctx.html("Error 404 - No Meals Found!!")
        }
    }

    fun countAllMeals (ctx: Context) {
        val mealsCount = mealDao.getAll().size
        if (mealsCount != 0) {
            ctx.status(200).json(mealsCount)
        } else {
            ctx.status(404).json("Error 404 - No Meals Found!!")
        }
    }

    fun getMealsByMealId (ctx: Context) {
        val meal = mealDao.findByMealId(ctx.pathParam("meal-id").toInt())
        if (meal != null) {
            ctx.status(200).json(meal)
        } else {
            ctx.status(404).html("No meals found")
        }
    }
    fun getMealsByUserId (ctx: Context) {
        val meal = mealDao.findMealByUserId(ctx.pathParam("user-id").toInt())
        if (meal.isNotEmpty()) {
            ctx.status(200).json(meal)
        } else {
            ctx.status(404).html("No meals found")
        }
    }


    fun getMealIngredients (ctx: Context) {
        val meal = mealDao.findByMealId(ctx.pathParam("meal-id").toInt())
        if (meal != null) {
            val ingredients = ingredientDao.findIngredientsForMeal(meal.id)
            if (ingredients.size != 0) {
                ctx.status(200).json(ingredients)
            } else {
                ctx.status(404).json("No ingredients found")
            }
        } else {
            ctx.status(404).html("No Meal found")
        }
    }

    fun addMeal (ctx: Context) {
        val mealDTO : MealDTO = jsonToObject(ctx.body())
        val userId = mealDTO.userId?.let { userDao.findById(it) }
        if (userId != null) {
            val mealId = mealDao.save(mealDTO)
            if (mealId != null) {
                mealDTO.id = mealId
                ctx.status(201).json(mealDTO)
            }
        } else {
            ctx.status(409).json("Could not add meal")
        }
    }

    fun updateMeal (ctx: Context) {
        val foundId = ctx.pathParam("meal-id").toInt()
        val meal : MealDTO = jsonToObject(ctx.body())
        if (mealDao.updateByMealId(mealId = foundId, mealDTO = meal) != 0) {
            ctx.status(204).json("Meal with id: $foundId, updated successfully")
        } else {
            ctx.status(404).json("Could not find meal with id: $foundId")
        }
    }

    fun deleteMealByMealId (ctx: Context) {
        val foundId = ctx.pathParam("meal-id").toInt()
        if (mealDao.findByMealId(foundId) != null) {
            mealDao.deleteByMealId(foundId)
            ctx.status(204).html("Meal with id: $foundId, deleted successfully")
        } else {
            ctx.status(404).json("Meal with id $foundId, does not exist")
        }
    }

    fun assignIngredientIdToMealId (ctx: Context) {
        val foundMealId = ctx.pathParam("meal-id").toInt()
        val foundIngredientId = ctx.pathParam("ingredient-id").toInt()
        print("meal: $foundMealId, ingredient: $foundIngredientId")
        if (mealDao.findByMealId(foundMealId) != null) {
            if (ingredientDao.findByIngredientId(foundIngredientId) != null) {
                val mealIngredient: MealIngredientDTO = jsonToObject("{\"mealId\":\"$foundMealId\", \"ingredientId\":\"$foundIngredientId\"}")
                mealDao.saveMealAndIngredientId(mealIngredient)
                saveNutrientsForMeals(foundMealId)
                ctx.status(200)
            } else {
                ctx.status(404).json("Ingredient with id $foundIngredientId, does not exist")
            }
        } else {
            ctx.status(404).json("Meal with id $foundMealId, does not exist")
        }
    }

    fun incrementMealLoves (ctx: Context) {
        val foundId = ctx.pathParam("meal-id").toInt()
        val foundMeal = mealDao.findByMealId(foundId)
        if (foundMeal != null) {
            val increment = foundMeal.loves?.plus(1)
            val meal: MealDTO = jsonToObject("{\"loves\":\"$increment\"}")
            if (mealDao.updateByMealId(mealId = foundId, mealDTO = meal) != 0) {
                ctx.status(201).json("Successfully incremented love, new value = $increment")
            }
        } else {
            ctx.status(404).json("Could not find the meal with id: $foundId")
        }
    }


    //--------------------------------------------------------------
    // IngredientDAO specifics
    //-------------------------------------------------------------

    fun getAllIngredients(ctx: Context) {
        val ingredients = ingredientDao.getAll()
        if (ingredients.size != 0) {
            ctx.status(200).json(ingredients)
        } else {
            ctx.status(404)
            ctx.html("Error 404 - No Ingredients Found!!")
        }
    }

    fun getIngredientsByIngredientId (ctx: Context) {
        val ingredient = ingredientDao.findByIngredientId(ctx.pathParam("ingredient-id").toInt())
        if (ingredient != null) {
            ctx.status(200).json(ingredient)
        } else {
            ctx.status(404).html("No ingredients found")
        }
    }

    fun addIngredient (ctx: Context) {
        val ingredient : IngredientDTO = jsonToObject(ctx.body())
        val ingredientId = ingredientDao.save(ingredient)
        if (ingredientId != null) {
            ctx.json(ingredient)
            ctx.status(201)
        } else {
            ctx.status(409).json("Could not add ingredient")
        }
    }

    fun updateIngredient (ctx: Context) {
        val ingredient : IngredientDTO = jsonToObject(ctx.body())
        if (ingredientDao.updateByIngredientId(ingredientId = ctx.pathParam("ingredient-id").toInt(), ingredientDTO = ingredient) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun deleteIngredientByIngredientId (ctx: Context) {
        val foundId = ctx.pathParam("ingredient-id").toInt()
        if (ingredientDao.findByIngredientId(foundId) != null) {
            ingredientDao.deleteByIngredientId(foundId)
            ctx.status(204).html("Ingredient with id: $foundId, deleted successfully")
        } else {
            ctx.status(404).json("Ingredient with id $foundId, does not exist")
        }
    }

    fun countAllMealIngredients (ctx: Context) {
        val foundId = ctx.pathParam("meal-id").toInt()
        if (mealDao.findByMealId(foundId) != null) {
            val count = ingredientDao.countAmountOfIngredientsInMeal(foundId)
            ctx.status(200).json(count)
        } else {
            ctx.status(404).json("Meal with id $foundId, does not exist")
        }
    }

    private fun saveNutrientsForMeals (mealId: Int) {
        val sumEnergy = ingredientDao.countEnergyForMeal(mealId)
        val sumCalories = ingredientDao.countCaloriesForMeal(mealId)
        val sumProtein = ingredientDao.countProteinForMeal(mealId)
        val sumFat = ingredientDao.countFatForMeal(mealId)
        val sumCarbs = ingredientDao.countCarbsForMeal(mealId)
        val sumSodium = ingredientDao.countSodiumForMeal(mealId)
        val meal: MealDTO = jsonToObject("{\"energy\":\"$sumEnergy\", \"calories\":\"$sumCalories\", \"protein\":\"$sumProtein\"," +
                " \"fat\":\"$sumFat\", \"carbs\":\"$sumCarbs\", \"sodium\":\"$sumSodium\"}")
        mealDao.updateByMealId(mealId, meal)
    }

    /**
     *
     * New feature
     * Make a route and method to save the bmi score to database which can be used to graph
     *
     */

    fun getBmiScoresByUserId (ctx: Context) {
        val foundId = ctx.pathParam("user-id").toInt()
        val bmiScores = userBmiDAO.findBmiScoresByUserId(foundId)
        if (bmiScores.size != 0) {
            ctx.status(200).json(bmiScores)
        } else {
            ctx.status(404).json("Bmi score with id $foundId, does not exist")
        }
    }

    fun addBmiScore (ctx: Context) {
        val bmi : UserBmiDTO = jsonToObject(ctx.body())
        val bmiId = userBmiDAO.save(bmi)
        if (bmiId != null) {
            ctx.json(bmi)
            ctx.status(201)
        } else {
            ctx.status(409).json("Could not add bmi score")
        }
    }

}