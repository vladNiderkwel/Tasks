package org.example

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object TodosService {
    private const val URL = "https://todo.doczilla.pro/api/todos"
    private val client = OkHttpClient()

    fun all(limit: Int?, offset: Int?): Response {
        val allUrl = URL.toHttpUrl().newBuilder().apply {
            if (limit != null) addQueryParameter("limit", limit.toString())
            if (offset != null) addQueryParameter("offset", offset.toString())
        }
            .build()

        val req = request(allUrl)

        return client.newCall(req).execute()
    }

    fun find(q: String, limit: Int?, offset: Int?): Response {
        val findUrl = URL.toHttpUrl().newBuilder().apply {
            addQueryParameter("q", q)
            if (limit != null) addQueryParameter("limit", limit.toString())
            if (offset != null) addQueryParameter("offset", offset.toString())
        }
            .build()

        val req = request(findUrl)

        return client.newCall(req).execute()
    }

    fun date(
        from: Int, to: Int, status: Boolean? = false,
        limit: Int?, offset: Int?
    ): Response {

        val dateUrl = URL.toHttpUrl().newBuilder().apply {
            addQueryParameter("from", from.toString())
            addQueryParameter("to", to.toString())
            if (status != null) addQueryParameter("status", status.toString())
            if (limit != null) addQueryParameter("limit", limit.toString())
            if (offset != null) addQueryParameter("offset", offset.toString())
        }
            .build()

        val req = request(dateUrl)

        return client.newCall(req).execute()
    }

    private fun request(url: HttpUrl) = Request.Builder()
        .url(url)
        .header("Accept", "application/json")
        .build()
}