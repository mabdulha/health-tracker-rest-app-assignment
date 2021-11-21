package org.wit.config

import com.harium.dotenv.Env
import org.jetbrains.exposed.sql.Database
import mu.KotlinLogging
import org.jetbrains.exposed.sql.name

val getHost: String = if (Env.get("DB_HOST") != null) {
    Env.get("DB_HOST")
} else {
    System.getenv("DB_HOST")
}
val getPort: String = if (Env.get("DB_PORT") != null) {
    Env.get("DB_PORT")
} else {
    System.getenv("DB_PORT")
}
val getDatabase: String = if (Env.get("DB_DATABASE") != null) {
    Env.get("DB_DATABASE")
} else {
    System.getenv("DB_DATABASE")
}
val getUser: String = if (Env.get("DB_USER") != null) {
    Env.get("DB_USER")
} else {
    System.getenv("DB_USER")
}
val getPassword: String = if (Env.get("DB_PASSWORD") != null) {
    Env.get("DB_PASSWORD")
} else {
    System.getenv("DB_PASSWORD")
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