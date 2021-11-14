package org.wit.utilities

import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.Exercises
import org.wit.db.Ingredients
import org.wit.db.Meals
import org.wit.db.Users
import org.wit.domain.ExerciseDTO
import org.wit.domain.IngredientDTO
import org.wit.domain.MealDTO
import org.wit.domain.UserDTO

fun mapToUserDTO(it: ResultRow) = UserDTO (
    id = it[Users.id],
    fname = it[Users.fname],
    lname = it[Users.lname],
    email = it[Users.email],
    password = it[Users.password],
    weight = it[Users.weight],
    height = it[Users.height],
    gender = it[Users.gender],
    age = it[Users.age]
)

fun mapToExerciseDTO(it: ResultRow) = ExerciseDTO (
    id = it[Exercises.id],
    image = it[Exercises.image],
    name = it[Exercises.name],
    description = it[Exercises.description],
    calories = it[Exercises.calories],
    duration = it[Exercises.duration],
    muscle = it[Exercises.muscle],
    views = it[Exercises.views],
    userId = it[Exercises.userId]
)

fun mapToMealDTO(it: ResultRow) = MealDTO (
    id = it[Meals.id],
    name = it[Meals.name],
    calories = it[Meals.calories],
    protein = it[Meals.protein],
    fat = it[Meals.fat],
    carbs = it[Meals.carbs]
)

fun mapToIngredientDTO(it: ResultRow) = IngredientDTO (
    id = it[Ingredients.id],
    name = it[Ingredients.name],
    energy = it[Ingredients.energy],
    calories = it[Ingredients.calories],
    protein = it[Ingredients.protein],
    fat = it[Ingredients.fat],
    carbs = it[Ingredients.carbs],
    sodium = it[Ingredients.sodium]
)