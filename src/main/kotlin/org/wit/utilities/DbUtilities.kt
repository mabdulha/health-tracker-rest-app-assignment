package org.wit.utilities

import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.*
import org.wit.domain.*

fun mapToUserDTO(it: ResultRow) = UserDTO (
    id = it[Users.id],
    avatar = it[Users.avatar],
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
    image = it[Meals.image],
    name = it[Meals.name],
    energy = it[Meals.energy],
    calories = it[Meals.calories],
    protein = it[Meals.protein],
    fat = it[Meals.fat],
    carbs = it[Meals.carbs],
    sodium = it[Meals.sodium],
    loves = it[Meals.loves],
    userId = it[Meals.userId]
)

fun mapToIngredientDTO(it: ResultRow) = IngredientDTO (
    id = it[Ingredients.id],
    image = it[Ingredients.image],
    name = it[Ingredients.name],
    energy = it[Ingredients.energy],
    calories = it[Ingredients.calories],
    protein = it[Ingredients.protein],
    fat = it[Ingredients.fat],
    carbs = it[Ingredients.carbs],
    sodium = it[Ingredients.sodium]
)

fun mapToUserBmiDTO (it: ResultRow) = UserBmiDTO (
    id = it[UserBmi.id],
    bmi = it[UserBmi.bmi],
    userId = it[UserBmi.userId]
)