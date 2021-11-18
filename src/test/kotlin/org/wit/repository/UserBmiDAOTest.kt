package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.helpers.populateUserBmiTable
import org.wit.helpers.populateUserTable
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
    inner class CreateUserBmi{

        @Test
        fun `multiple bmis added to table can be retrieved successfully` () {
            transaction {

                //Arrange - create and populate table with three users and user bmi's
                populateUserTable()
                val userBmiDAO = populateUserBmiTable()

                //Act & Assert
                assertEquals(3, userBmiDAO.getAll().size)
                assertEquals(2, userBmiDAO.findBmiScoresByUserId(user1.id).size)

            }
        }
    }

    @Nested
    inner class ReadUserBmi{

        @Test
        fun `getting all user bmi's from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users and user bmi's
                populateUserTable()
                val userBmiDAO = populateUserBmiTable()

                //Act & Assert
                assertEquals(3, userBmiDAO.getAll().size)
            }
        }

    }

}