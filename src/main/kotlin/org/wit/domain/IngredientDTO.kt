package org.wit.domain

data class IngredientDTO (
    var id: Int,
    var name: String,
    var energy: Int,
    var protein: Double,
    var carbs: Double,
    var fat : Double,
    var sodium: Double
)