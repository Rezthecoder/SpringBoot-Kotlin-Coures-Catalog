package com.kotlinspring.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CourseDTO(
    val id : Int?,
    @get:NotBlank(message = "courseDTO.name cannot be blank")
    val name : String,
    @get:NotBlank(message = "courseDTO.category cannot be blank")
    val category : String,
    @get:NotNull(message = "courseDTO.instructorId cannot be null")
    val instructorId : Int?=null,
)