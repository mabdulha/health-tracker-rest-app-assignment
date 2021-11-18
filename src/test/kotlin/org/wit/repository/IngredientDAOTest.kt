package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.db.Ingredients
import org.wit.domain.IngredientDTO
import org.wit.helpers.*

//retrieving some test data from Fixtures
val ingredient1 = ingredients[0]
val ingredient2 = ingredients[1]
val ingredient3 = ingredients[2]

val meal1 = meals[0]
val meal2 = meals[1]
val meal3 = meals[2]

val mealIngredient1 = mealIngredients[0]
val mealIngredient2 = mealIngredients[1]
val mealIngredient3 = mealIngredients[2]


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
        fun `get all ingredients over empty table returns none`() {
            transaction {

                //Arrange - create and setup ingredientDAO object
                SchemaUtils.create(Ingredients)
                val ingredientDAO = IngredientDAO()

                //Act & Assert
                assertEquals(0, ingredientDAO.getAll().size)
            }
        }

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

        @Test
        fun `updating non-existent ingredient in table results in no updates`() {
            transaction {

                //Arrange - create and populate table with three ingredients
                val ingredientDao = populateIngredientTable()

                //Act & Assert
                val ingredient4Updated = IngredientDTO(id = 4, image = "https://agile-dev-2021.netlify.app/topic06-testing-unit/book-01-unit-testing/img/main5.png", name = "Lamb", energy = 9, calories = 99, protein = 9.99, fat = 9.99, carbs = 9.99, sodium = 9.99)
                ingredientDao.updateByIngredientId(4, ingredient4Updated)
                assertEquals(null, ingredientDao.findByIngredientId(4))
                assertEquals(3, ingredientDao.getAll().size)
            }
        }

    }

    @Nested
    inner class DeleteIngredients {

        @Test
        fun `deleting a non-existent ingredient in table results in no deletion`() {
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

    @Nested
    inner class IngredientCounts {

        @Test
        fun `return all the ingredients associated with a meal by meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()
                val ingredients = arrayListOf(ingredient1, ingredient2)

                assertEquals(ingredients, ingredientDAO.findIngredientsForMeal(meal1.id))
                assertEquals(arrayListOf(ingredient3), ingredientDAO.findIngredientsForMeal(meal2.id))

            }
        }

        @Test
        fun `count the amount of ingredients in meals with correct meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()

                assertEquals(2, ingredientDAO.countAmountOfIngredientsInMeal(meal1.id))

            }
        }

        @Test
        fun `count the amount of energy in a meal by passing meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()
                val count = ingredient1.energy?.plus(ingredient2.energy!!)

                assertEquals(count, ingredientDAO.countEnergyForMeal(meal1.id))
                assertEquals(ingredient3.energy, ingredientDAO.countEnergyForMeal(meal2.id))

            }
        }

        @Test
        fun `count the amount of calories in a meal by passing meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()
                val count = ingredient1.calories?.plus(ingredient2.calories!!)


                assertEquals(count, ingredientDAO.countCaloriesForMeal(meal1.id))
                assertEquals(ingredient3.calories, ingredientDAO.countCaloriesForMeal(meal2.id))

            }
        }

        @Test
        fun `count the amount of protein in a meal by passing meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()
                val count = ingredient1.protein?.plus(ingredient2.protein!!)


                assertEquals(count, ingredientDAO.countProteinForMeal(meal1.id))
                assertEquals(ingredient3.protein, ingredientDAO.countProteinForMeal(meal2.id))

            }
        }

        @Test
        fun `count the amount of fat in a meal by passing meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()
                val count = ingredient1.fat?.plus(ingredient2.fat!!)


                assertEquals(count, ingredientDAO.countFatForMeal(meal1.id))
                assertEquals(ingredient3.fat, ingredientDAO.countFatForMeal(meal2.id))

            }
        }

        @Test
        fun `count the amount of carbs in a meal by passing meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()
                val count = ingredient1.carbs?.plus(ingredient2.carbs!!)


                assertEquals(count, ingredientDAO.countCarbsForMeal(meal1.id))
                assertEquals(ingredient3.carbs, ingredientDAO.countCarbsForMeal(meal2.id))

            }
        }

        @Test
        fun `count the amount of sodium in a meal by passing meal id` () {
            transaction {

                //Arrange - create and populate table with three ingredients, meals, users and ids for mealIngredients
                populateUserTable()
                val ingredientDAO = populateIngredientTable()
                populateMealTable()
                populateMealIngredientTable()
                val count = ingredient1.sodium?.plus(ingredient2.sodium!!)


                assertEquals(count, ingredientDAO.countSodiumForMeal(meal1.id))
                assertEquals(ingredient3.sodium, ingredientDAO.countSodiumForMeal(meal2.id))

            }
        }

    }

}