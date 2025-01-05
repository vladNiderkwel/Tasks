package org.example

import spark.Filter
import spark.Spark.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    after(Filter { _, response ->
        with(response) {
            header("Access-Control-Allow-Origin", "*")
            header("Access-Control-Allow-Methods", "GET,POST,DELETE")
            header("Content-Type", "application/json")
            status(200)
        }
    })

    options("/*") { _, resp ->
        resp.status(200)
    }

    // Получение всех студентов
    get("/student") { _, _ ->
        Json.encodeToString(StudentService.all())
    }

    // Удаление студента по id
    delete("/student/:id") { req, _ ->
        val id = req.params("id").toInt()
        Json.encodeToString(StudentService.delete(id))
    }

    // Добавление нового студента
    post("/student") { req, _ ->
        val student = Json.decodeFromString<Student>(req.body())
        StudentService.create(student)
    }
}