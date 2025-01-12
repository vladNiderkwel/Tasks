package org.example

import org.eclipse.jetty.http.HttpStatus
import spark.Filter
import spark.Spark.*

fun main() {
    println("Server is running on http://localhost:4567")

    after(Filter { _, response ->
        with(response) {
            header("Access-Control-Allow-Origin", "*")
            header("Access-Control-Allow-Methods", "GET")
            header("Content-Type", "application/json")
        }
    })

    options("/*") { _, resp ->
        resp.status(200)
    }

    // Получение заданий
    get("/") { req, resp ->
        try {
            val limit = req.queryParams("limit")
            val offset = req.queryParams("offset")

            val allResponse = TodosService.all(limit?.toInt(), offset?.toInt())

            with(allResponse) {
                resp.status(code)
                resp.body(body?.string())
            }
        }
        catch (e: Exception) {
            resp.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
            println(e)
            throw e
        }
    }

    // Поиск заданий по строке
    get("/find") { req, resp ->
        try {
            val q = req.queryParams("q")
            val limit = req.queryParams("limit")
            val offset = req.queryParams("offset")

            val findResponse = TodosService.find(q, limit?.toInt(), offset?.toInt())

            with(findResponse) {
                resp.status(code)
                resp.body(body?.string())
            }
        }
        catch (e: Exception) {
            resp.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
            println(e)
            throw e
        }
    }

    // Поиск заданий по датам
    get("/date") { req, resp ->
        try {
            val from = req.queryParams("from")
            val to = req.queryParams("to")
            val status = req.queryParams("status")
            val limit = req.queryParams("limit")
            val offset = req.queryParams("offset")

            val dateResponse = TodosService.date(
                from = from.toLong(),
                to = to.toLong(),
                status = status?.toBoolean(),
                limit = limit?.toInt(),
                offset = offset?.toInt()
            )

            with(dateResponse) {
                resp.status( code )
                resp.body( body?.string() )
            }
        }
        catch (e: Exception) {
            resp.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
            println(e)
            throw e
        }
    }
}