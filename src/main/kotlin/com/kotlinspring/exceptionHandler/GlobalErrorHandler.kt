package com.kotlinspring.exceptionHandler

import com.kotlinspring.exception.InstructorIdNotValidException
import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.Objects

@Component
@ControllerAdvice
class GlobalErrorHandler: ResponseEntityExceptionHandler() {

    companion object:KLogging()
    override fun handleMethodArgumentNotValid(


        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {

        logger.error("handleMethodArgumentNotValid is ${ex.message}",ex)
       var errors = ex.bindingResult.allErrors
            .map {
                error-> error.defaultMessage!!
            }
            .sorted()
        logger.info(errors)
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors.joinToString(", "){it})
    }

    @ExceptionHandler(Exception::class)
fun  handleAllExceptions(ex:Exception,request: WebRequest):ResponseEntity<Any>{
      logger.error("handleAllExceptions occurred is ${ex.message}",ex)

    return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ex.message)

}

    @ExceptionHandler(InstructorIdNotValidException::class)
    fun  handleInstrutorNotValidExceptions(ex:InstructorIdNotValidException,request: WebRequest):ResponseEntity<Any>{
        logger.error("Exception occurred is ${ex.message}",ex)

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ex.message)

    }

}