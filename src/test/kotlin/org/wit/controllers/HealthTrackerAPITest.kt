package org.wit.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.wit.config.DbConfig
import org.wit.controllers.HealthTrackerAPI.updateUser
import org.wit.domain.UserDTO
import org.wit.helpers.*
import org.wit.utilities.jsonToObject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerAPITest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    //helper function to add a test user to the database
    private fun addUser (fname: String, lname: String, email: String, password: String, weight: Double, height: Float, age: Int, gender: Char): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users")
            .body("{\"fname\":\"$fname\", \"lname\":\"$lname\", \"email\":\"$email\", \"password\":\"$password\", \"weight\":\"$weight\", \"height\":\"$height\", \"age\":\"$age\", \"gender\":\"$gender\"}")
            .asJson()
    }

    //helper function to delete a test user from the database
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id").asString()
    }

    //helper function to retrieve a test user from the database by email
    private fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        return Unirest.get("$origin/api/users/email/${email}").asString()
    }

    //helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int) : HttpResponse<String> {
        return Unirest.get("$origin/api/users/${id}").asString()
    }

    //helper function to add a test user to the database
    private fun updateUser (id: Int, name: String, email: String, password: String): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"fname\":\"$name\", \"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()
    }

    private fun loginUser (email: String, password: String): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users/login")
            .body("{\"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()
    }

    @Nested
    inner class ReadUsers {

        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/users/").asString()
            if (response.status == 200) {
                val retrievedUsers: ArrayList<UserDTO> = jsonToObject(response.body.toString())
                Assert.assertNotEquals(0, retrievedUsers.size)
            }
            else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/users/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/users/email/${nonExistingEmail}").asString()
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)
        }

        @Test
        fun `getting a user by email when email exists, returns a 200 response`() {

            //Arrange - add the user
            addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : UserDTO = jsonToObject(retrieveResponse.body.toString())
            deleteUser(retrievedUser.id)
        }

    }

    @Nested
    inner class CreateUsers {

        @Test
        fun `add a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse= retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
            val retrievedUser : UserDTO = jsonToObject(addResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validFName, retrievedUser.fname)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse = deleteUser(retrievedUser.id)
            assertEquals(204, deleteResponse.status)
        }

    }

    @Nested
    inner class UpdateUsers {

        private val updatedFName = "Updated FName"
        private val updatedEmail = "updatedemail@gmail.com"
        private val updatedPassword = "newsecret"

        @Test
        fun `updating a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do an update on
            val addedResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
            assertEquals(204, updateUser(addedUser.id, updatedFName, updatedEmail, updatedPassword).status)

            //Act & Assert - retrieve updated user and assert details are correct
            val updatedUserResponse = retrieveUserById(addedUser.id)
            val updatedUser : UserDTO = jsonToObject(updatedUserResponse.body.toString())
            assertEquals(updatedFName, updatedUser.fname)
            assertEquals(updatedEmail, updatedUser.email)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateUser(-1, updatedFName, updatedEmail, updatedPassword).status)
        }

    }

    @Nested
    inner class DeleteUsers {

        @Test
        fun `deleting a user when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteUser(-1).status)
        }

        @Test
        fun `deleting a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveUserById(addedUser.id).status)
        }

    }

    @Nested
    inner class LoginUser {

        @Test
        fun `Logging in a user with correct credentials` () {

            //Arrange - add the user that we plan to do a login on
            val addedResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            assertEquals(200, loginUser(addedUser.email, addedUser.password).status)

        }

    }

}