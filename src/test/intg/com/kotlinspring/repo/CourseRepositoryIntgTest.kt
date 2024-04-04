package com.kotlinspring.controller.repo

import com.kotlinspring.courseEntityList
import com.kotlinspring.repository.CourseRepository
import net.bytebuddy.asm.Advice.Argument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {
    @Autowired
lateinit var courseRepository: CourseRepository


    @BeforeEach
    fun  setUp(){
        courseRepository.deleteAll()
        var courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {
       var courses = courseRepository.findByNameContaining("python")
        println("courses is $courses")
        Assertions.assertEquals(1,courses.size)

    }

    @Test
    fun findByCourseName() {
        var courses = courseRepository.findCourseByName("python")
        println("courses is $courses")
        Assertions.assertEquals(1,courses.size)

    }


    @ParameterizedTest
    @MethodSource("nameAndSize")
    fun findByCourseName_approach2(name: String,size:Int) {
        var courses = courseRepository.findCourseByName("python")
        println("courses is $courses")
        Assertions.assertEquals(1,courses.size)
    }
    companion object{

        @JvmStatic
        fun nameAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("python", 1),
                Arguments.arguments("java", 2)
            )
        }

    }

}
