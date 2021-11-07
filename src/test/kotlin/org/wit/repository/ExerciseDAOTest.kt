package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.wit.helpers.exercises
import org.wit.helpers.populateExerciseTable
import org.wit.helpers.populateUserTable

//retrieving some test data from Fixtures
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

    @Nested
    inner class CreateExercises {

        @Test
        fun `multiple exercises added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three exercises
                val userDAO = populateUserTable()
                val activityDAO = populateExerciseTable()
                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                assertEquals(exercise1, activityDAO.findByExerciseId(exercise1.id))
                assertEquals(exercise2, activityDAO.findByExerciseId(exercise2.id))
                assertEquals(exercise3, activityDAO.findByExerciseId(exercise3.id))
            }
        }
    }

}
