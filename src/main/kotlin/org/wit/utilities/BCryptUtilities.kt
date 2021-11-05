package org.wit.utilities

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(plaintext: String?): String {
    return BCrypt.hashpw(plaintext, BCrypt.gensalt())
}

fun decryptPassword(plaintext: String?, hashedtext: String?): Boolean {
    return BCrypt.checkpw(plaintext, hashedtext)
}