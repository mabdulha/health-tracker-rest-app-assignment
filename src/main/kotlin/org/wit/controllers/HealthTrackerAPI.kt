package org.wit.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.harium.dotenv.Env
import io.javalin.http.Context
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.wit.domain.ExerciseDTO
import org.wit.domain.IngredientDTO
import org.wit.domain.MealDTO
import org.wit.domain.UserDTO
import org.wit.repository.ExerciseDAO
import org.wit.repository.IngredientDAO
import org.wit.repository.MealDAO
import org.wit.repository.UserDAO
import org.wit.utilities.decryptPassword
import org.wit.utilities.jsonToObject
import java.util.*

object HealthTrackerAPI {

    private val userDao = UserDAO()
    private val exerciseDao = ExerciseDAO()
    private val mealDao = MealDAO()
    private val ingredientDao = IngredientDAO()

    //--------------------------------------------------------------
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
            ctx.status(409).json("The email: ${user.email}, already exists")
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
        val user : UserDTO = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), userDTO=user)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
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

    //--------------------------------------------------------------
    // ExerciseDAO specifics
    //-------------------------------------------------------------

    fun getAllExercises (ctx: Context) {
        ctx.json(exerciseDao.getAll())
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
            ctx.status(404).html("No exercises found")
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
        val exercise : ExerciseDTO = jsonToObject(ctx.body())
        if (exerciseDao.updateByExerciseId(exerciseId = ctx.pathParam("exercise-id").toInt(), exerciseDTO = exercise) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun incrementView (ctx: Context) {
        val foundId = ctx.pathParam("exercise-id").toInt()
        val foundExercise = exerciseDao.findByExerciseId(foundId)
        if (foundExercise != null) {
            val increment = foundExercise.views + 1
            val exercise: ExerciseDTO = jsonToObject("{\"views\":\"$increment\"}")
            if (exerciseDao.updateByExerciseId(exerciseId = foundId, exerciseDTO = exercise) != 0) {
                ctx.status(204).json("Successfully incremented view, new value = $increment")
            }
        } else {
            ctx.status(404).json("Could not find the exercise with id: $foundId")
        }
    }

    fun deleteExerciseByExerciseId (ctx: Context) {
        val foundId = ctx.pathParam("exercise-id").toInt()
        if (exerciseDao.findByExerciseId(foundId) != null) {
            exerciseDao.deleteByExerciseId(foundId)
            ctx.status(204).html("Exercise with id: $foundId, deleted successfully")
        } else {
            ctx.status(404).json("Exercise with id $foundId, does not exist")
        }
    }

    fun deleteExerciseByUserId (ctx: Context) {
        val foundId = ctx.pathParam("user-id").toInt()
        if (exerciseDao.deleteByExerciseId(foundId) != 0) {
            ctx.status(204).html("Exercises belonging to user with id: $foundId, deleted successfully")
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

    fun getMealsByMealId (ctx: Context) {
        val meal = mealDao.findByMealId(ctx.pathParam("meal-id").toInt())
        if (meal != null) {
            ctx.status(200).json(meal)
        } else {
            ctx.status(404).html("No meals found")
        }
    }

    fun addMeal (ctx: Context) {
        val meal : MealDTO = jsonToObject(ctx.body())
        val mealId = mealDao.save(meal)
        if (mealId != null) {
            ctx.json(meal)
            ctx.status(201)
        } else {
            ctx.status(409).json("Could not add meal")
        }
    }

    fun updateMeal (ctx: Context) {
        val meal : MealDTO = jsonToObject(ctx.body())
        if (mealDao.updateByMealId(mealId = ctx.pathParam("meal-id").toInt(), mealDTO = meal) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
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

}