package org.wit.config

import org.jetbrains.exposed.sql.Database
import mu.KotlinLogging
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{
        logger.info{"Starting DB Connection..."}

        val dbConfig = Database.connect(
            "jdbc:postgresql://ec2-3-231-103-217.compute-1.amazonaws.com:5432/d71v0ptt0jppju?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "wgqkbqgwqbhcta",
            password = "71801c40346d5f3fe7e367257334859fe984f0b08a01346a55837b6112972958")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}