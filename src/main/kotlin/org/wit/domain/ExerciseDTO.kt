package org.wit.domain

data class ExerciseDTO (
    var id: Int,
    var image: String?,
    var name: String?,
    var description: String?,
    var calories: Int?,
    var duration: Double?,
    var muscle: String?,
    var views: Int,
    var userId: Int?
    )