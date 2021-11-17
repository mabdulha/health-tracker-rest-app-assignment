package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
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

}