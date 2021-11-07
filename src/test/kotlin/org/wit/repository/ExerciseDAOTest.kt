package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.wit.helpers.exercises

val exercise1 = exercises[0]
val exercise2 = exercises[1]
val exercise3 = exercises[2]

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

}
