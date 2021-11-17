package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.db.Ingredients
import org.wit.helpers.ingredients

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
                SchemaUtils.create(Ingredients)
                val ingredientDao = IngredientDAO()
                ingredientDao.save(ingredient1)
                ingredientDao.save(ingredient2)
                ingredientDao.save(ingredient3)

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
                SchemaUtils.create(Ingredients)
                val ingredientDao = IngredientDAO()
                ingredientDao.save(ingredient1)
                ingredientDao.save(ingredient2)
                ingredientDao.save(ingredient3)

                //Act & Assert
                assertEquals(3, ingredientDao.getAll().size)
            }
        }

        @Test
        fun `get ingredient by id that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                SchemaUtils.create(Ingredients)
                val ingredientDao = IngredientDAO()
                ingredientDao.save(ingredient1)
                ingredientDao.save(ingredient2)
                ingredientDao.save(ingredient3)

                //Act & Assert
                assertEquals(null, ingredientDao.findByIngredientId(4))
            }
        }

        @Test
        fun `get ingredient by id that exists, results in a correct ingredient returned`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                SchemaUtils.create(Ingredients)
                val ingredientDao = IngredientDAO()
                ingredientDao.save(ingredient1)
                ingredientDao.save(ingredient2)
                ingredientDao.save(ingredient3)

                //Act & Assert
                assertEquals(ingredient3, ingredientDao.findByIngredientId(3))
            }

        }

    }

    @Nested
    inner class UpdateIngredients {}

    @Nested
    inner class DeleteIngredients {}

}