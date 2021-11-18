package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.db.Meals
import org.wit.domain.MealDTO
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
                assertEquals(3, mealDAO.getAll().size)
                assertEquals(meal1, mealDAO.findByMealId(meal1.id))
                assertEquals(meal2, mealDAO.findByMealId(meal2.id))
                assertEquals(meal3, mealDAO.findByMealId(meal3.id))

            }
        }

    }

    @Nested
    inner class ReadMeals {

        @Test
        fun `getting all meals from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(3, mealDAO.getAll().size)
            }
        }

        @Test
        fun `get all meals over empty table returns none`() {
            transaction {

                //Arrange - create and setup mealDAO object and populate with users
                populateUserTable()
                SchemaUtils.create(Meals)
                val mealDAO = MealDAO()

                //Act & Assert
                assertEquals(0, mealDAO.getAll().size)
            }
        }

        @Test
        fun `get meal by id that doesn't exist, results in no meal returned`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(null, mealDAO.findByMealId(4))
            }
        }

        @Test
        fun `get meal by id that exists, results in a correct meal returned`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(meal1, mealDAO.findByMealId(1))
                assertEquals(meal2, mealDAO.findByMealId(2))
                assertEquals(meal3, mealDAO.findByMealId(3))
            }
        }

        @Test
        fun `get meal by user id that doesn't exist, results in no meal returned`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(0, mealDAO.findMealByUserId(4).size)
            }
        }

        @Test
        fun `get meal by user id that exists, results in a correct meal returned`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()
                val meals = arrayListOf(meal2, meal1)

                //Act & Assert
                assertEquals(meals, mealDAO.findMealByUserId(1))
                assertEquals(arrayListOf(meal3), mealDAO.findMealByUserId(2))
            }
        }

    }

    @Nested
    inner class UpdateMeals {

        @Test
        fun `updating existing meal in table results in successful update`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                val meal3updated = MealDTO(
                    id = 3, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main3.png",
                    name = "Chicken Soup", energy = 0, calories = 0, protein = 0.00, fat = 0.00, carbs = 0.00, sodium = 0.00,
                    loves = 46, userId = 2
                )
                mealDAO.updateByMealId(meal3updated.id, meal3updated)
                assertEquals(meal3updated, mealDAO.findByMealId(3))
            }
        }

        @Test
        fun `updating non-existent meal in table results in no updates`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                val meal4updated = MealDTO(
                    id = 3, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main3.png",
                    name = "Chicken Soup", energy = 0, calories = 0, protein = 0.00, fat = 0.00, carbs = 0.00, sodium = 0.00,
                    loves = 46, userId = 2
                )
                mealDAO.updateByMealId(4, meal4updated)
                assertEquals(null, mealDAO.findByMealId(4))
                assertEquals(3, mealDAO.getAll().size)
            }
        }

    }

    @Nested
    inner class DeleteMeals {

        @Test
        fun `deleting a non-existent meal in table results in no deletion`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(3, mealDAO.getAll().size)
                mealDAO.deleteByMealId(4)
                assertEquals(3, mealDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing meal in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users and meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(3, mealDAO.getAll().size)
                mealDAO.deleteByMealId(user3.id)
                assertEquals(2, mealDAO.getAll().size)
            }
        }

    }

}