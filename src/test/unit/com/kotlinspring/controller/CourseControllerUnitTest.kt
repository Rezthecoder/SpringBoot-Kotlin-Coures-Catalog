package com.kotlinspring.controller

import com.kotlinspring.courseDTO
import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import org.springframework.test.web.reactive.server.WebTestClient


@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMokk: CourseService


    @Test
    fun addCourse() {
        var courseDTO = CourseDTO(null, "hello world", "rez",1)

        every {courseServiceMokk.addCourse(any())} returns courseDTO(id=1)
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
    fun addCourse_validation() {
        var courseDTO = CourseDTO(null, "", "",1)

        every {courseServiceMokk.addCourse(any())} returns courseDTO(id=1)
        val savedCourse = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("courseDTO.category cannot be blank, courseDTO.name cannot be blank",savedCourse)
    }

    @Test
    fun addCourse_runTimeException() {
        var courseDTO = CourseDTO(null, "hello world", "rez",1)
var errorMessage = "unexpected error occured"
        every {courseServiceMokk.addCourse(any())} throws RuntimeException(errorMessage)

        val savedCourse = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("unexpected error occured",savedCourse)
    }



    @Test
    fun retriveAllCourses() {

        every { courseServiceMokk.retriveAllCourses(any()) }.returnsMany(
            listOf(
                courseDTO(id=1),
                courseDTO(id=2)

            )
        )


        var courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        println(courseDTOs)
        Assertions.assertEquals(2,courseDTOs!!.size)
    }



    @Test
    fun retriveAllCoursesByName() {

        every { courseServiceMokk.retriveAllCourses(any()) }.returnsMany(
            listOf(
                courseDTO(id=1),
                courseDTO(id=2)

            )
        )


        var courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        println(courseDTOs)
        Assertions.assertEquals(2,courseDTOs!!.size)
    }




    @Test
    fun updateCourse() {
        var course = Course(null,"hello world","rez")

        every { courseServiceMokk.updateCourse(any(),any()) } returns courseDTO(id = 100,
            name = "hello world2")

        var updatesCourseDTO=  CourseDTO(null,"hello world2","rez")

        webTestClient.put()
            .uri("/v1/courses/{courseId}",100)
            .bodyValue(updatesCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("hello world2",updatesCourseDTO.name)

    }

    @Test
    fun deleteCourse() {
        every {
            courseServiceMokk.deleteCourse(any()) } just  runs

        var updatesCourseDTO= webTestClient
            .delete()
            .uri("/v1/courses/{courseId}",100)
            .exchange()
            .expectStatus().isNoContent


    }


}