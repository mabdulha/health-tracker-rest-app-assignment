package org.wit.config

import com.harium.dotenv.Env
import org.jetbrains.exposed.sql.Database
import mu.KotlinLogging
import org.jetbrains.exposed.sql.name

val getHost: String = Env.get("DB_HOST")
val getPort: String = Env.get("DB_PORT")
val getDatabase: String = Env.get("DB_DATABASE")
val getUser: String = Env.get("DB_USER")
val getPassword: String = Env.get("DB_PASSWORD")

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{
        logger.info{"Starting DB Connection..."}

        val dbConfig = Database.connect(
            "jdbc:postgresql://$getHost:$getPort/$getDatabase?sslmode=require",
            driver = "org.postgresql.Driver",
            user = getUser,
            password = getPassword)

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}