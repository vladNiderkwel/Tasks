package org.example

import java.sql.Date

object StudentService {
    private val QUERY_ALL = "SELECT * FROM ${DBConnection.TABLE_STUDENTS}"
    private val QUERY_DELETE = "DELETE FROM ${DBConnection.TABLE_STUDENTS} WHERE ${DBConnection.FIELD_ID} = ?"
    private val QUERY_CREATE = """
        INSERT INTO ${DBConnection.TABLE_STUDENTS} 
            (${DBConnection.FIELD_NAME}, ${DBConnection.FIELD_SURNAME}, 
                ${DBConnection.FIELD_LASTNAME}, ${DBConnection.FIELD_BIRTH}, ${DBConnection.FIELD_GROUP})
                    VALUES(?, ?, ?, ?, ?);
    """.trimIndent()

    fun all(): List<Student> {
        val list = mutableListOf<Student>()
        with( DBConnection.connection.prepareStatement(QUERY_ALL).executeQuery() ) {
            while(next()) {
                list.add(
                    Student(
                        id = getInt(DBConnection.FIELD_ID),
                        name = getString(DBConnection.FIELD_NAME),
                        surname = getString(DBConnection.FIELD_SURNAME),
                        lastname = getString(DBConnection.FIELD_LASTNAME),
                        birth = getDate(DBConnection.FIELD_BIRTH).toLocalDate(),
                        group = getString(DBConnection.FIELD_GROUP)
                    )
                )
            }
        }
        return list
    }

    fun delete(id: Int): Int {
        val query = DBConnection.connection.prepareStatement(QUERY_DELETE)
        query.setInt(1, id)
        return query.executeUpdate()
    }

    fun create(student: Student): Int {
        val query = DBConnection.connection.prepareStatement(QUERY_CREATE).apply {
            setString(1, student.name)
            setString(2, student.surname)
            setString(3, student.lastname)
            setDate(4, Date.valueOf(student.birth))
            setString(5, student.group)
        }
        return query.executeUpdate()
    }
}