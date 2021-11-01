package org.wit.controllers

import org.junit.jupiter.api.TestInstance
import org.wit.config.DbConfig
import org.wit.helpers.ServerContainer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerAPITest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

}