package com.kotlinspring.controller

import com.kotlinspring.courseEntityList
import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.instructorEntity
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {


    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun  setUp(){
        courseRepository.deleteAll()
        instructorRepository.deleteAll()
        var instructor = instructorEntity()
        instructorRepository.save(instructor)
       var courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }


    @Test
    fun retriveAllCourses() {
       var courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
println(courseDTOs)
      Assertions.assertEquals(3,courseDTOs!!.size)
    }


    @Test
    fun addCourse() {
     var instructorEntity =  instructorRepository.findAll().first()

        var courseDTO = CourseDTO(null, "hello world using kotlin", "rez programmer",instructorEntity.id)

        val savedCourse = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody


        Assertions.assertTrue(
            savedCourse!!.id != null
        )


    }

    @Test
    fun updateCourse() {
        var instructorEntity =  instructorRepository.findAll().first()

        var course = Course(null,"hello world","rez",instructorEntity)
        courseRepository.save(course)
      var updatesCourseDTO=  CourseDTO(null,"hello world1","rez",course.instructor!!.id)

        webTestClient.put()
            .uri("/v1/courses/{courseId}",course.id)
            .bodyValue(updatesCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("hello world1",updatesCourseDTO.name)

    }



    @Test
    fun deleteCourse() {
        var instructorEntity =  instructorRepository.findAll().first()

        var course = Course(null,"hello world","rez",instructorEntity)
        courseRepository.save(course)

        var updatesCourseDTO= webTestClient
            .delete()
            .uri("/v1/courses/{courseId}",course.id)
            .exchange()
            .expectStatus().isNoContent


    }

}