package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.wit.db.Exercises
import org.wit.domain.ExerciseDTO
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
                val exerciseDAO = populateExerciseTable()
                //Act & Assert
                assertEquals(3, exerciseDAO.getAll().size)
                assertEquals(exercise1, exerciseDAO.findByExerciseId(exercise1.id))
                assertEquals(exercise2, exerciseDAO.findByExerciseId(exercise2.id))
                assertEquals(exercise3, exerciseDAO.findByExerciseId(exercise3.id))
            }
        }
    }

    @Nested
    inner class ReadExercises {

        @Test
        fun `getting all exercises from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three exercises
                val userDAO = populateUserTable()
                val exerciseDAO = populateExerciseTable()
                //Act & Assert
                assertEquals(3, exerciseDAO.getAll().size)
            }
        }

        @Test
        fun `get exercise by user id that has no exercises, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three exercises
                val userDAO = populateUserTable()
                val exerciseDAO = populateExerciseTable()
                //Act & Assert
                assertEquals(0, exerciseDAO.findExerciseByUserId(3).size)
            }
        }

        @Test
        fun `get exercise by user id that exists, results in a correct exercise(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three exercises
                populateUserTable()
                val exerciseDAO = populateExerciseTable()
                //Act & Assert
                assertEquals(exercise1, exerciseDAO.findExerciseByUserId(1)[0])
                assertEquals(exercise2, exerciseDAO.findExerciseByUserId(1)[1])
                assertEquals(exercise3, exerciseDAO.findExerciseByUserId(2)[0])
            }
        }

        @Test
        fun `get all exercises over empty table returns none`() {
            transaction {

                //Arrange - create and setup exerciseDAO object
                SchemaUtils.create(Exercises)
                val exerciseDAO = ExerciseDAO()

                //Act & Assert
                assertEquals(0, exerciseDAO.getAll().size)
            }
        }

        @Test
        fun `get exercise by exercise id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three exercises
                populateUserTable()
                val exerciseDAO = populateExerciseTable()
                //Act & Assert
                assertEquals(null, exerciseDAO.findByExerciseId(4))
            }
        }

        @Test
        fun `get exercise by exercise id that exists, results in a correct exercise returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three exercises
                populateUserTable()
                val exerciseDAO = populateExerciseTable()
                //Act & Assert
                assertEquals(exercise1, exerciseDAO.findByExerciseId(1))
                assertEquals(exercise3, exerciseDAO.findByExerciseId(3))
            }
        }

    }

    @Nested
    inner class UpdateExercises {

        @Test
        fun `updating existing exercise in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three exercises
                populateUserTable()
                val exerciseDAO = populateExerciseTable()

                //Act & Assert
                val exercise3updated = ExerciseDTO(
                    id = 3, name = "Running", description = "Cardio", duration = 42.0,
                    calories = 220, muscle = "Body", userId = 2, views = 25, image = ""
                )
                exerciseDAO.updateByExerciseId(exercise3updated.id, exercise3updated)
                assertEquals(exercise3updated, exerciseDAO.findByExerciseId(3))
            }
        }

        @Test
        fun `updating non-existant exercise in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three exercises
                populateUserTable()
                val exerciseDAO = populateExerciseTable()

                //Act & Assert
                val exercise4updated = ExerciseDTO(id = 4, name = "Running", description = "Cardio", duration = 42.0,
                    calories = 220, muscle = "Body", userId = 2, views = 35, image = "")
                exerciseDAO.updateByExerciseId(4, exercise4updated)
                assertEquals(null, exerciseDAO.findByExerciseId(4))
                assertEquals(3, exerciseDAO.getAll().size)
            }
        }

    }

    @Nested
    inner class DeleteExercises {

        @Test
        fun `deleting a non-existant exercise (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three exercises
                val userDAO = populateUserTable()
                val exerciseDAO = populateExerciseTable()

                //Act & Assert
                assertEquals(3, exerciseDAO.getAll().size)
                exerciseDAO.deleteByExerciseId(4)
                assertEquals(3, exerciseDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing exercise (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three exercises
                val userDAO = populateUserTable()
                val exerciseDAO = populateExerciseTable()

                //Act & Assert
                assertEquals(3, exerciseDAO.getAll().size)
                exerciseDAO.deleteByExerciseId(exercise3.id)
                assertEquals(2, exerciseDAO.getAll().size)
            }
        }

        @Test
        fun `deleting exercises when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three exercises
                val userDAO = populateUserTable()
                val exerciseDAO = populateExerciseTable()

                //Act & Assert
                assertEquals(3, exerciseDAO.getAll().size)
                exerciseDAO.deleteByUserId(3)
                assertEquals(3, exerciseDAO.getAll().size)
            }
        }

        @Test
        fun `deleting exercises when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three exercises
                val userDAO = populateUserTable()
                val exerciseDAO = populateExerciseTable()

                //Act & Assert
                assertEquals(3, exerciseDAO.getAll().size)
                exerciseDAO.deleteByUserId(1)
                assertEquals(1, exerciseDAO.getAll().size)
            }
        }

    }

}
