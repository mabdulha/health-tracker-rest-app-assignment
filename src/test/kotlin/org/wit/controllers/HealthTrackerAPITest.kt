package org.wit.controllers

import kong.unirest.Unirest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.wit.config.DbConfig
import org.wit.helpers.ServerContainer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerAPITest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Test
    fun `get all users from the database returns 200 or 404 response`() {
        val response = Unirest.get("$origin/api/users/").asString()
        assertEquals(200, response.status)
    }

}