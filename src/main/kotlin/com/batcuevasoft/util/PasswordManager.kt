package com.batcuevasoft.util

import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom
import kotlin.random.asKotlinRandom


object PasswordManager : PasswordManagerContract {

    private val letters = 'a'..'z'
    private val uppercaseLetters = 'A'..'Z'
    private val numbers = '0'..'9'
    private const val special: String = "@#=+!£\$%&?"

    override fun generatePasswordWithDefault() = generatePassword()

    /**
     * Generate a random password
     * @param isWithLetters Boolean value to specify if the password must contain letters
     * @param isWithUppercase Boolean value to specify if the password must contain uppercase letters
     * @param isWithNumbers Boolean value to specify if the password must contain numbers
     * @param isWithSpecial Boolean value to specify if the password must contain special chars
     * @param length Int value with the length of the password
     * @return the new password.
     */
    fun generatePassword(
        isWithLetters: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = true,
        length: Int = 6
    ): String {
        var chars = ""

        if (isWithLetters) {
            chars += letters
        }
        if (isWithUppercase) {
            chars += uppercaseLetters
        }
        if (isWithNumbers) {
            chars += numbers
        }
        if (isWithSpecial) {
            chars += special
        }

        val rnd = SecureRandom.getInstance("SHA1PRNG").asKotlinRandom()
        return List(length) { chars.random(rnd) }.joinToString("")
    }

    override fun validatePassword(attempt: String, userPassword: String): Boolean {
        return BCrypt.checkpw(attempt, userPassword)
    }

    override fun encryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

}

interface PasswordManagerContract {
    fun validatePassword(attempt: String, userPassword: String): Boolean
    fun encryptPassword(password: String): String
    fun generatePasswordWithDefault(): String
}