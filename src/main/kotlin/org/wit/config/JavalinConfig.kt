package org.wit.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.wit.controllers.HealthTrackerAPI

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create().apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getHerokuAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getHerokuAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin){
        app.routes {
            //User routes
            get("/api/users", HealthTrackerAPI::getAllUsers)
            get("/api/users/:user-id", HealthTrackerAPI::getUserByUserId)
            post("/api/users", HealthTrackerAPI::addUser)
            get("/api/users/email/:email", HealthTrackerAPI::getUserByEmail)
            delete("/api/users/:user-id", HealthTrackerAPI::deleteUser)
            patch("/api/users/:user-id", HealthTrackerAPI::updateUser)
            post("/api/users/login/", HealthTrackerAPI::login)

            //Exercise routes
            get("/api/exercises", HealthTrackerAPI::getAllExercises)
            get("/api/users/:user-id/exercises", HealthTrackerAPI::getExercisesByUserId)
            get("/api/exercises/:exercise-id", HealthTrackerAPI::getExercisesByExerciseId)
            post("/api/exercises", HealthTrackerAPI::addExercise)
            patch("/api/exercises/:exercise-id", HealthTrackerAPI::updateExercise)
            put("/api/exercises/:exercise-id/increment-view", HealthTrackerAPI::incrementView)
            delete("/api/exercises/:exercise-id", HealthTrackerAPI::deleteExerciseByExerciseId)
            delete("/api/users/:user-id/exercises", HealthTrackerAPI::deleteExerciseByUserId)

            //Meal routes
            get("/api/meals", HealthTrackerAPI::getAllMeals)
            get("/api/meals/:meal-id", HealthTrackerAPI::getMealsByMealId)
            post("/api/meals", HealthTrackerAPI::addMeal)
        }
    }

}