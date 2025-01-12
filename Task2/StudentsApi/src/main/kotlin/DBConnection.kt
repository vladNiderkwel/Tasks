package org.example

import java.sql.Connection
import java.sql.DriverManager

object DBConnection {
    private const val DB = "mydb"
    const val TABLE_STUDENTS = "Students"

    const val FIELD_ID = "id"
    const val FIELD_NAME = "name"
    const val FIELD_SURNAME = "surname"
    const val FIELD_LASTNAME = "lastname"
    const val FIELD_BIRTH = "birth"
    const val FIELD_GROUP = "class"

    private const val DB_DRIVER = "org.h2.Driver"
    private const val DB_URL = "jdbc:h2:mem:~/mydb;DB_CLOSE_DELAY=-1;"
    private const val DB_USER = "sa"

    val connection : Connection = connect()

    init {
        initDb()
    }

    private fun initDb() {
        try {
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS $DB;").execute()
            connection.prepareStatement("SET SCHEMA $DB;").execute()
            connection.prepareStatement("DROP TABLE IF EXISTS $TABLE_STUDENTS;").execute()
            connection.prepareStatement("""
                CREATE TABLE $TABLE_STUDENTS (
                    $FIELD_ID INT auto_increment,
                    $FIELD_NAME VARCHAR(100) NOT NULL,
                    $FIELD_SURNAME VARCHAR(100) NOT NULL,
                    $FIELD_LASTNAME VARCHAR(100) NOT NULL,
                    $FIELD_BIRTH DATE NOT NULL,
                    $FIELD_GROUP VARCHAR(100) NOT NULL
                );""".trimIndent()
            ).executeUpdate()

            connection.prepareStatement("""
                INSERT INTO $TABLE_STUDENTS($FIELD_NAME, $FIELD_SURNAME, $FIELD_LASTNAME, $FIELD_BIRTH, $FIELD_GROUP)
                    VALUES('Петя', 'Петячкин', 'Петрович', '2000-01-21', 'Группа 1');
            """.trimIndent()).executeUpdate()

            connection.prepareStatement("""
                INSERT INTO $TABLE_STUDENTS($FIELD_NAME, $FIELD_SURNAME, $FIELD_LASTNAME, $FIELD_BIRTH, $FIELD_GROUP)
                    VALUES('Вася', 'Васечкин', 'Васильевич', '1999-03-20', 'Группа 2'); 
            """.trimIndent()).executeUpdate()

            connection.prepareStatement("""
                INSERT INTO $TABLE_STUDENTS($FIELD_NAME, $FIELD_SURNAME, $FIELD_LASTNAME, $FIELD_BIRTH, $FIELD_GROUP)
                    VALUES('Павел', 'Павлов', 'Павлович', '2002-04-25', 'Группа 3');
            """.trimIndent()).executeUpdate()
        }
        catch (e: Exception) {
            println(e)
            throw e
        }
    }

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