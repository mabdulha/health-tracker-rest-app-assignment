package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.domain.IngredientDTO
import org.wit.helpers.ingredients
import org.wit.helpers.populateIngredientTable

//retrieving some test data from Fixtures
val ingredient1 = ingredients[0]
val ingredient2 = ingredients[1]
val ingredient3 = ingredients[2]

class IngredientDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateIngredients {

        @Test
        fun `multiple ingredients added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDao = populateIngredientTable()

                //Act & Assert
                assertEquals(3, ingredientDao.getAll().size)
                assertEquals(ingredient1, ingredientDao.findByIngredientId(ingredient1.id))
                assertEquals(ingredient2, ingredientDao.findByIngredientId(ingredient2.id))
                assertEquals(ingredient3, ingredientDao.findByIngredientId(ingredient3.id))
            }
        }

    }

    @Nested
    inner class ReadIngredients {

        @Test
        fun `getting all ingredients from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDao = populateIngredientTable()

                //Act & Assert
                assertEquals(3, ingredientDao.getAll().size)
            }
        }

        @Test
        fun `get ingredient by id that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDao = populateIngredientTable()

                //Act & Assert
                assertEquals(null, ingredientDao.findByIngredientId(4))
            }
        }

        @Test
        fun `get ingredient by id that exists, results in a correct ingredient returned`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDao = populateIngredientTable()

                //Act & Assert
                assertEquals(ingredient3, ingredientDao.findByIngredientId(3))
            }

        }

    }

    @Nested
    inner class UpdateIngredients {

        @Test
        fun `updating existing ingredient in table results in successful update`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDao = populateIngredientTable()

                //Act & Assert
                val ingredient3Updated = IngredientDTO(id = 3, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main5.png", name = "Lamb", energy = 9, calories = 99, protein = 9.99, fat = 9.99, carbs = 9.99, sodium = 9.99)
                ingredientDao.updateByIngredientId(ingredient3.id, ingredient3Updated)
                assertEquals(ingredient3Updated, ingredientDao.findByIngredientId(3))
            }
        }

    }

    @Nested
    inner class DeleteIngredients {

        @Test
        fun `deleting a non-existant ingredient in table results in no deletion`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDAO = populateIngredientTable()

                //Act & Assert
                assertEquals(3, ingredientDAO.getAll().size)
                ingredientDAO.deleteByIngredientId(4)
                assertEquals(3, ingredientDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing ingredient in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDAO = populateIngredientTable()

                //Act & Assert
                assertEquals(3, ingredientDAO.getAll().size)
                ingredientDAO.deleteByIngredientId(user3.id)
                assertEquals(2, ingredientDAO.getAll().size)
            }
        }

    }

}