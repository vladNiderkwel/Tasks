package org.example

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    // Ввод пути сохранения итогово файла
    println("Please enter save destination for concatenated file:")
    val destination = readln()

    // Ввод пути корневой папки
    println("Please enter directory path to scan:")
    val checkDir = readln()

    // Получение отсортированного списка файлов
    val files = Files.walk( Path.of(checkDir) )
        .filter(Files::isRegularFile)
        .sorted()
        .toList()

    // Регекс для обнаружения зависимостей в файле
    val requireRegex = "require ‘.*?’".toRegex()

    // Мапа зависимостей и файлов, в которых они появляются
    val dependencies: MutableMap<Path, MutableList<Path>> = mutableMapOf<Path, MutableList<Path>>().apply {
        files.forEach { path -> set(path, mutableListOf()) }
    }

    files.forEach { path ->
        val matches = requireRegex.findAll(path.toFile().readText())

        val requirements = matches.map { match ->
            files.find { pth ->
                pth.endsWith(match.value.drop(9).dropLast(1))
            } ?: throw Exception("Requirement of such path not found")
        }.toList()

        requirements.forEach { req ->
            dependencies[req]?.add(path) ?: throw Exception("No such path")
        }
    }

    // Сортировка по зависимостям
    val result = sortByRequirements(dependencies)

    // Конкатенация всех файлов в один
    File("$destination\\concatenated-file.txt")
        .printWriter().use { writer ->
            result.forEach { path ->
                writer.println("\n------------------${path.fileName}------------------\n")
                writer.println(path.toFile().readText())
            }
        }
}

fun sortByRequirements(dependencies: Map<Path, List<Path>>): List<Path> {
    val visited = mutableListOf<Path>()
    val result = mutableSetOf<Path>()

    fun visit(path: Path) {
        visited.add(path)

        dependencies[path]!!.forEach { req ->
            if (!visited.contains(req))
                visit(req)
            else if (!result.contains(req))
                throw Exception("Infinity loop in ${req.fileName}")
        }

        result.add(path)
    }

    dependencies.forEach { (path, _) ->
        if (!visited.contains(path))
            visit(path)
    }

    return result.reversed()
}