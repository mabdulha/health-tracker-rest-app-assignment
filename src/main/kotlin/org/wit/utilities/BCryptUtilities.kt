package org.wit.utilities

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(plaintext: String, genSaltRounds: Int = 10): String {
    return BCrypt.hashpw(plaintext, BCrypt.gensalt(genSaltRounds))
}

fun decryptPassword(plaintext: String, hashedtext: String): Boolean {
    return BCrypt.checkpw(plaintext, hashedtext)
}