package org.wit.domain

data class UserDTO (
    var id: Int,
    var fname: String?,
    var lname: String?,
    var email: String,
    var password: String,
    var weight: Double?,
    var height: Float?,
    var gender: Char?,
    var age: Int?
    )