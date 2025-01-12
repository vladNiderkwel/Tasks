package org.example

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Student(
    val id: Int = -1,
    val name: String,
    val surname: String,
    val lastname: String,
    @Serializable(with = DateSerializer::class)
    val birth: LocalDate,
    val group: String
)
