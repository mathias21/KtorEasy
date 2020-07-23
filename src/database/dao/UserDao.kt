package com.batcuevasoft.database.dao

import com.batcuevasoft.model.PostUserBody
import com.batcuevasoft.model.PutUserBody
import com.batcuevasoft.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

object Users : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val username = varchar("username", 255)
    val name = varchar("name", 255)
    val secondname = varchar("secondname", 255)
    val creationTime = long("creationTime")
    val password = varchar("password", 255)

    fun getUserById(userId: Int): User? {
        return select {
            (id eq userId)
        }.mapNotNull {
            it.mapRowToUser()
        }.singleOrNull()
    }

    fun insertUser(postUser: PostUserBody): InsertStatement<Number> {
        return insert {
            it[name] = postUser.name
            it[secondname] = postUser.secondname
            it[username] = postUser.username
            it[password] = postUser.password
            it[creationTime] = System.currentTimeMillis()
        }
    }

    fun updateUser(userId: Int, putUser: PutUserBody): User? {
        update({ id eq userId }) { user ->
            putUser.name?.let { user[name] = it }
            putUser.secondname?.let { user[secondname] = it }
            putUser.username?.let { user[username] = it }
        }
        return getUserById(userId)
    }

    fun deleteUser(userId: Int): Boolean {
        return deleteWhere { (id eq userId) } > 0
    }

    fun getUserByName(usernameValue: String): User? {
        return select {
            (username eq usernameValue)
        }.mapNotNull {
            it.mapRowToUser()
        }.singleOrNull()
    }
}

fun ResultRow.mapRowToUser() =
    User(
        id = this[Users.id],
        name = this[Users.name],
        secondname = this[Users.secondname],
        username = this[Users.username],
        password = this[Users.password]
    )