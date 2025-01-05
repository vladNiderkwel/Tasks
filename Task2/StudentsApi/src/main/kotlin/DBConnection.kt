package org.example

import java.sql.Connection
import java.sql.DriverManager

object DBConnection {
    val TABLE_STUDENTS = "Students"

    val FIELD_ID = "id"
    val FIELD_NAME = "name"
    val FIELD_SURNAME = "surname"
    val FIELD_LASTNAME = "lastname"
    val FIELD_BIRTH = "birth"
    val FIELD_GROUP = "class"

    private val DB_DRIVER = "org.h2.Driver"
    private val DB_URL = "jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'dbschema.sql'"
    private val DB_USER = "sa"

    val connection : Connection = connect()

    private fun connect(): Connection {
        try {
            Class.forName(DB_DRIVER)
            return DriverManager.getConnection(DB_URL, DB_USER, "")
        }
        catch (e: Exception) {
            println(e)
            throw e
        }
    }
}