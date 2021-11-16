package org.wit.domain

data class MealDTO (
    var id : Int,
    var image: String?,
    var name: String?,
    var energy: Int? = 0,
    var calories: Int? = 0,
    var protein: Double? = 0.00,
    var fat: Double? = 0.00,
    val carbs: Double? = 0.00,
    var sodium: Double? = 0.00,
    var loves: Int? = 0,
    val userId: Int?
)