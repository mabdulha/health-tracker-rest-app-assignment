package org.wit.helpers

import org.wit.domain.UserDTO

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validFName = "Michael"
val validEmail = "testuser1@test.com"

val users = arrayListOf(
    UserDTO(fname = "Alice", lname = "Cullen", email = "alice@cullen.com", password = "secret", weight = 2.0, height = 170f, gender = 'M', age = 25, id = 1),
    UserDTO(fname = "Bob", lname = "Cat", email = "bob@cat.ie", password = "secret", weight = 2.0, height = 1.555f, gender = 'F', age = 18, id = 2),
    UserDTO(fname = "Mary", lname = "Contrary", email = "mary@contrary.com", password = "secret", weight = 2.0, height = 1.555f, gender = 'F', age = 55, id = 3),
    UserDTO(fname = "Carol", lname = "Singer", email = "carol@singer.com", password = "secret", weight = 2.0, height = 1.555f, gender = 'M', age = 32, id = 4)
)