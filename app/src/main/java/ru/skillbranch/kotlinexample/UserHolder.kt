package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

/**
 * Created by rolea on 21.07.2020.
 */
object UserHolder {

    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User = User.makeUser(fullName, email, password)
        .also { user ->
            if (map[user.login] != null) throw IllegalArgumentException("A user with this email already exists")
            map[user.login] = user
        }

    fun registerUserByPhone(fullName: String, rawPhone: String): User = User.makeUser(
        fullName,
        phone = rawPhone
    ).also { user ->
        if (map[user.login] != null) throw IllegalArgumentException("A user with this phone already exists")
        map[user.login] = user
    }

    fun loginUser(login: String, password: String): String? {
        val loginFormatted = if (User.isValidPhone(login)) User.createPhone(login) else login.trim()

        return map[loginFormatted]?.let {
            if (it.checkPassword(password)) it.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) {
        val loginFormatted = if (User.isValidPhone(login)) User.createPhone(login) else login.trim()
        map[loginFormatted]?.changeAccessCode()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}