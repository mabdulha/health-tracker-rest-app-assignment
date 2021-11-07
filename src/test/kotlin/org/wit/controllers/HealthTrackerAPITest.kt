package org.wit.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.wit.config.DbConfig
import org.wit.domain.ExerciseDTO
import org.wit.domain.UserDTO
import org.wit.helpers.*
import org.wit.utilities.jsonToObject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerAPITest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    //--------------------------------------------------------------
    // User Tests
    //-------------------------------------------------------------

    @Nested
    inner class ReadUsers {

        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get("$origin/api/users/").asString()
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

        @Test
        fun `add a user where email already exists, returns a 409` () {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            //Arrange - add the user that we plan to do a login on
            val addedResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            assertEquals(409, addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender).status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)

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
    inner class LoginUsers {

        private val inValidEmail = "bwefubef@ugfrb.ie"
        private val inValidPassword = "iuhwefnr"

        @Test
        fun `logging in a user with correct credentials, returns a 200 response` () {

            //Arrange - add the user that we plan to do a login on
            val addedResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            assertEquals(200, loginUser(addedUser.email, addedUser.password).status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)

        }

        @Test
        fun `logging in a user with incorrect email and password, returns a 401 response` () {

            //Arrange - add the user that we plan to do a login on
            val addedResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            assertEquals(401, loginUser(inValidEmail, inValidPassword).status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)

        }

        @Test
        fun `logging in a user with correct email and incorrect password, returns a 401 response` () {

            //Arrange - add the user that we plan to do a login on
            val addedResponse = addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight,
                validAge, validGender)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            assertEquals(401, loginUser(addedUser.email, inValidPassword).status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)

        }

    }

    //--------------------------------------------------------------
    // Exercises Tests
    //-------------------------------------------------------------

    @Nested
    inner class CreateExercises {
        //   post(  "/api/exercises", HealthTrackerAPI::addExercise)
        @Test
        fun `add an activity when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: UserDTO = jsonToObject(addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight, validAge, validGender).body.toString())

            val addExerciseResponse = addExercise(exercises[0].name ,exercises[0].description,
                exercises[0].calories, exercises[0].duration, exercises[0].muscle, addedUser.id)
            assertEquals(201, addExerciseResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add an exercise when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addExerciseResponse = addExercise(
                exercises[0].name ,exercises[0].description,
                exercises[0].calories, exercises[0].duration, exercises[0].muscle, userId
            )
            assertEquals(404, addExerciseResponse.status)
        }

    }

    @Nested
    inner class ReadExercises {

        @Test
        fun `get all exercises from the database returns 200 or 404 response`() {
            val response = retrieveAllExercises()
            if (response.status == 200){
                val retrievedExercises : ArrayList<ExerciseDTO> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedExercises.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all exercises by user id when user and exercise exists returns 200 response`() {
            //Arrange - add a user and 3 associated exercises that we plan to retrieve
            val addedUser : UserDTO = jsonToObject(addUser(validFName, validLName, validEmail, validPassword, validWeight, validHeight, validAge, validGender).body.toString())
            addExercise(exercises[0].name ,exercises[0].description,
                exercises[0].calories, exercises[0].duration, exercises[0].muscle, addedUser.id)
            addExercise(exercises[1].name ,exercises[1].description,
                exercises[1].calories, exercises[1].duration, exercises[1].muscle, addedUser.id)
            addExercise(exercises[2].name ,exercises[2].description,
                exercises[2].calories, exercises[2].duration, exercises[2].muscle, addedUser.id)

            //Assert and Act - retrieve the three added exercises by user id
            val response = retrieveExercisesByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedExercises : ArrayList<ExerciseDTO> = jsonToObject(response.body.toString())
            assertEquals(3, retrievedExercises.size)

            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class UpdateExercises {
        //  patch( "/api/exercises/:exercise-id", HealthTrackerAPI::updateExercise)
    }

    @Nested
    inner class DeleteExercises {
        //   delete("/api/exercises/:exercise-id", HealthTrackerAPI::deleteExerciseByExerciseId)
        //   delete("/api/users/:user-id/exercises", HealthTrackerAPI::deleteExerciseByUserId)
    }

    //--------------------------------------------------------------
    // User Helper Classes
    //-------------------------------------------------------------

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

    private fun retrieveAllUsers () : HttpResponse<String> {
        return Unirest.get("$origin/api/users").asString()
    }

    //helper function to add a test user to the database
    private fun updateUser (id: Int, name: String, email: String, password: String): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/users/$id")
            .body("{\"fname\":\"$name\", \"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()
    }

    private fun loginUser (email: String?, password: String?): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users/login")
            .body("{\"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()
    }

    //--------------------------------------------------------------
    // Exercise Helper Classes
    //-------------------------------------------------------------

    //helper function to retrieve all exercises
    private fun retrieveAllExercises(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/exercises").asJson()
    }

    //helper function to retrieve exercises by user id
    private fun retrieveExercisesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/users/${id}/exercises").asJson()
    }

    //helper function to retrieve exercise by exercise id
    private fun retrieveExerciseByExerciseId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/exercises/${id}").asJson()
    }

    //helper function to delete an exercise by exercise id
    private fun deleteExercisesByExerciseId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/exercises/$id").asString()
    }

    //helper function to delete an exercise by user id
    private fun deleteExercisesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id/exercises").asString()
    }

    //helper function to update a test exercise to the database
    private fun updateExercise(id: Int?, name: String?, description: String?, calories: Int?, duration: Double?,
                               muscle: String?, userId: Int?): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/exercises/$id")
            .body("{\"name\":\"$name\", \"description\":\"$description\", \"calories\":\"$calories\", \"duration\":\"$duration\", \"muscle\":\"$muscle\", \"userId\":\"$userId\"}")
            .asJson()
    }

    //helper function to add an exercise
    private fun addExercise(name: String?, description: String?, calories: Int?, duration: Double?,
                            muscle: String?, userId: Int?): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/exercises")
            .body("{\"name\":\"$name\", \"description\":\"$description\", \"calories\":\"$calories\", \"duration\":\"$duration\", \"muscle\":\"$muscle\", \"userId\":\"$userId\"}")
            .asJson()
    }


}