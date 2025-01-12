package org.example

import spark.Filter
import spark.Spark.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    println("Server is running on http://localhost:4567")

    options("/*") { _, resp ->
        resp.status(200)
    }

    // Получение всех студентов
    get("/student") { _, resp ->
        resp.status(200)
        Json.encodeToString(StudentService.all())
    }

    // Удаление студента по id
    delete("/student/:id") { req, resp ->
        val id = req.params("id").toInt()
        resp.status(200)
        Json.encodeToString(StudentService.delete(id))
    }

    // Добавление нового студента
    post("/student") { req, resp ->
        val student = Json.decodeFromString<Student>(req.body())
        resp.status(200)
        StudentService.create(student)
    }

    after(Filter { _, response ->
        with(response) {
            header("Access-Control-Allow-Origin", "*")
            header("Access-Control-Allow-Headers", "Content-Type")
            header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE")
            header("Content-Type", "application/json")
        }
    })
}