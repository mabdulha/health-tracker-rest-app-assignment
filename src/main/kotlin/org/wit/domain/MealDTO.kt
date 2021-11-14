package org.wit.domain

data class MealDTO (
    var id : Int,
    var image: String?,
    var name: String?,
    var energy: Int?,
    var calories: Int?,
    var protein: Double?,
    var fat: Double?,
    val carbs: Double?,
    var sodium: Double?,
    var loves: Int?
)