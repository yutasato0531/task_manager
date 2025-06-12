package com.example.task_manager


import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RestController
class SessionsController(
    @Autowired private val repository: SessionsRepository
) {
    @GetMapping("/sessions/cookie")
    fun setCookie(request: HttpServletRequest?, response: HttpServletResponse): String {
        val sessionId: String = getRandomString(16)
        val cookie: Cookie = Cookie("sessionId", sessionId)
        cookie.setSecure(false)
        cookie.setHttpOnly(false)
        cookie.setMaxAge(60 * 60) // expires in 1 hour
        response.addCookie(cookie)
        return sessionId
    }

    @PostMapping("/sessions")
    fun setSession(@RequestBody request: SessionsRequest){
        val entity = SessionsEntity(sessionId = request.sessionId , userId = request.userId, userName = request.userName)
        repository.save(entity)
    }

    @DeleteMapping("/sessions/{userId}")
    fun deleteSession(@PathVariable userId: Long){
        repository.deleteById(userId)
        print("Sessions deleted")
    }

}