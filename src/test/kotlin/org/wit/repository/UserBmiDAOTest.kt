package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.wit.helpers.userBmi

val userBmi1 = userBmi[0]
val userBmi2 = userBmi[1]
val userBmi3 = userBmi[2]

class UserBmiDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateUserBmi{}

    @Nested
    inner class ReadUserBmi{}

}