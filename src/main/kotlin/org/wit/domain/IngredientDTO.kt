package org.wit.domain

data class IngredientDTO (
    var id: Int,
    var image: String?,
    var name: String?,
    var energy: Int?,
    var calories: Int?,
    var protein: Double?,
    var fat : Double?,
    var carbs: Double?,
    var sodium: Double?
)