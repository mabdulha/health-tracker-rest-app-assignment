package org.wit.repository

import org.wit.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.wit.db.Users
import org.wit.domain.UserDTO
import org.wit.helpers.nonExistingEmail
import org.wit.helpers.populateUserTable
import kotlin.test.assertNotEquals

//retrieving some test data from Fixtures
val user1 = users[0]
val user2 = users[1]
val user3 = users[2]

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateUsers {

        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                assertEquals(user1.email, userDAO.findByEmail(user1.email)?.email)
                assertEquals(user2.email, userDAO.findByEmail(user2.email)?.email)
                assertEquals(user3.email, userDAO.findByEmail(user3.email)?.email)
            }
        }

    }

    @Nested
    inner class ReadUsers {

        @Test
        fun `getting all users from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
            }
        }

        @Test
        fun `get all users over empty table returns none`() {
            transaction {

                //Arrange - create and setup userDAO object
                SchemaUtils.create(Users)
                val userDAO = UserDAO()

                //Act & Assert
                assertEquals(0, userDAO.getAll().size)
            }
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findByEmail(nonExistingEmail))
            }
        }

        @Test
        fun `get user by email that exists, results in correct user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(user2.email, userDAO.findByEmail(user2.email)?.email)
            }
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findById(4))
            }
        }

        @Test
        fun `get user by id that exists, results in a correct user returned`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findById(4))
            }
        }

    }

    @Nested
    inner class DeleteUsers {

        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(4)
                assertEquals(3, userDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(user3.id)
                assertEquals(2, userDAO.getAll().size)
            }
        }

    }

    @Nested
    inner class UpdateUsers {

        @Test
        fun `updating existing user in table results in successful update`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                val user3Updated = UserDTO(3, "", "Leroy", "Matthews", "leroy@gmail.com", "secretpass", 90.00, 154f, 'M', 54)
                userDAO.update(user3.id, user3Updated)
                assertEquals(user3Updated.id, userDAO.findById(3)?.id)
                assertEquals(user3Updated.avatar, userDAO.findById(3)?.avatar)
                assertEquals(user3Updated.fname, userDAO.findById(3)?.fname)
                assertEquals(user3Updated.lname, userDAO.findById(3)?.lname)
                assertEquals(user3Updated.email, userDAO.findById(3)?.email)
                assertNotEquals(user3Updated.password, userDAO.findById(3)?.password) // not equal because of the password hashing
                assertEquals(user3Updated.weight, userDAO.findById(3)?.weight)
                assertEquals(user3Updated.height, userDAO.findById(3)?.height)
                assertEquals(user3Updated.age, userDAO.findById(3)?.age)
            }
        }

        @Test
        fun `updating non-existant user in table results in no updates`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                val user4Updated = UserDTO(3, "", "Leroy", "Matthews", "leroy@gmail.com", "supersecret", 90.00, 154f, 'M', 54)
                userDAO.update(4, user4Updated)
                assertEquals(null, userDAO.findById(4))
                assertEquals(3, userDAO.getAll().size)
            }
        }

    }

}