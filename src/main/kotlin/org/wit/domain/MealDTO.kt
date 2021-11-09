package org.wit.domain

data class MealDTO (
    var id : Int,
    var name: String,
    var calories: Int?,
    var protein: Double?,
    var fat: Double?,
    val carbs: Double?
)