package org.wit.config

import com.harium.dotenv.Env
import org.jetbrains.exposed.sql.Database
import mu.KotlinLogging
import org.jetbrains.exposed.sql.name

val getHost: String = checkVars("DB_HOST")
val getPort: String = checkVars("DB_PORT")
val getDatabase: String = checkVars("DB_DATABASE")
val getUser: String = checkVars("DB_USER")
val getPassword: String = checkVars("DB_PASSWORD")

fun checkVars (variable: String): String {
    val string: String = if (Env.get(variable) == null) {
        System.getenv(variable)
    } else {
        Env.get(variable)
    }
    return string
}

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