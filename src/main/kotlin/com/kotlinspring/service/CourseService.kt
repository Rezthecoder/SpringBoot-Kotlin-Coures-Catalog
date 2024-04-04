package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorIdNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository,
    val instructorService: InstructorService) {

    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

     var instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)
     if (!instructorOptional.isPresent)  {

         throw InstructorIdNotValidException("instructor not valid ${courseDTO.instructorId}")
     }


        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category,instructorOptional.get())
        }

        courseRepository.save(courseEntity)

        logger.info("Saved course is : $courseEntity")

        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category,it.instructor!!.id)
        }

    }

    fun retriveAllCourses(courseName: String?): List<CourseDTO> {
       var courses= courseName?.let {
           courseRepository.findCourseByName(courseName)
       } ?: courseRepository.findAll()

        return courses
            .map {
                CourseDTO(it.id, it.name, it.category)
            }

    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        var existingCourse = courseRepository.findById(courseId)
        return if (existingCourse.isPresent) {
             existingCourse.get()
                .let {
//                    it.id = courseDTO.id
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category)
                }


        } else {
            throw CourseNotFoundException("course is not found of id $courseId")
        }

    }

    fun deleteCourse(courseId: Int) {
      var course =  courseRepository.findById(courseId)
        if (course.isPresent){
//            course.get()
//                .let {
//                    courseRepository.deleteById(courseId)
//                }
            courseRepository.deleteById(courseId)
        }
        else
        {
            throw   CourseNotFoundException("course is not found of id $courseId")
        }


    }
}

