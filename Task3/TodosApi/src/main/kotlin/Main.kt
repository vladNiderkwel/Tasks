package org.example

import org.eclipse.jetty.http.HttpStatus
import spark.Filter
import spark.Spark.*

fun main() {
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

    get("/date") { req, resp ->
        try {
            val from = req.queryParams("from")
            val to = req.queryParams("to")
            val limit = req.queryParams("limit")
            val offset = req.queryParams("offset")

            val dateResponse = TodosService.date(
                from = from.toInt(),
                to = to.toInt(),
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