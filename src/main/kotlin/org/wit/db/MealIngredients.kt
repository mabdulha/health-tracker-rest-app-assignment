package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MealIngredients: Table("meal_ingredients") {
    val mealId = integer("meal_id").references(Meals.id, onDelete = ReferenceOption.CASCADE)
    val ingredientId = integer("ingredient_id").references(Ingredients.id, onDelete = ReferenceOption.CASCADE)
}