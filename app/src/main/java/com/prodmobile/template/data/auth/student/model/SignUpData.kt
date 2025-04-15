package com.prodmobile.template.data.auth.student.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentSignUpData(
    @SerialName("full_name")
    val fullName: String,
    val age: Long?,
    val interests: List<String>,
    val bytes: ByteArray?
)


@Serializable
data class StudentSignUpRequestDto(
    @SerialName("full_name")
    val name: String,
    val age: Long? = null,
    val interests: List<String>
)