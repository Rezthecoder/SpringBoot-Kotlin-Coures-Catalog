package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository :CrudRepository<Instructor,Int>{

}
