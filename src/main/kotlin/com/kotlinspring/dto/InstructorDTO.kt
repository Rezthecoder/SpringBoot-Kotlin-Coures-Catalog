package com.kotlinspring.dto

import javax.validation.constraints.NotBlank


data class InstructorDTO(

    val id:Int?,
    @get:NotBlank(message = "instructorDTO.name cannot be blank")
    val name:String
) 
