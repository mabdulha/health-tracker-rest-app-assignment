package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.helpers.populateMealTable
import org.wit.helpers.populateUserTable

class MealDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateMeals {

        @Test
        fun `multiple meals added to table can be retrieved successfully` () {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                Assertions.assertEquals(3, mealDAO.getAll().size)
                Assertions.assertEquals(meal1, mealDAO.findByMealId(meal1.id))
                Assertions.assertEquals(meal2, mealDAO.findByMealId(meal2.id))
                Assertions.assertEquals(meal3, mealDAO.findByMealId(meal3.id))

            }
        }

    }

    @Nested
    inner class ReadMeals {

        @Test
        fun `getting all meals from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                Assertions.assertEquals(3, mealDAO.getAll().size)
            }
        }

    }

    @Nested
    inner class UpdateMeals {}

    @Nested
    inner class DeleteMeals {}

}