package com.example.task_manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import kotlin.jvm.optionals.toList

fun getRandomString(length: Int) : String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

fun generateSha256Hash(input: String): String {
    val bytes = input.toByteArray(Charsets.UTF_8)
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)

    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

@RestController
class UsersController(
    @Autowired private val repository: UsersRepository
) {
    @GetMapping("/users/get")
    fun getUser(): List<UsersEntity> {
        return repository.findAll().toList()
    }

    @GetMapping("/users/get/id/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<UsersEntity> {
        val user = repository.findById(id)
        return if (user.isPresent) {
            ResponseEntity.ok(user.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/users/get/name/{user_name}")
    fun getUserByName(@PathVariable("user_name") userName: String): Long? {
        val user = repository.findAll().filter { it.userName  == userName }.toList()
        return user[0].userId
    }

    @PostMapping("/users/post")
    fun postUser(@RequestBody user: UserRegRequest): String {
        val salt = getRandomString(10)
        val hash = generateSha256Hash(user.pass + salt)

        val entity = UsersEntity(
            userName = user.userName, firstName = user.firstName, lastName = user.lastName, salt = salt,  hash = hash)
        repository.save(entity)
        return ""
    }

    @PostMapping("/users/login")
    fun login(@RequestBody req: LoginRequest): Long? {
        val loginUser: List<UsersEntity> = repository.findAll().filter { it.userName == req.userName }.toList()

        if(loginUser.isEmpty()) {
            return 0
        }

        val requestedHash = generateSha256Hash(req.password + loginUser[0].salt)

        if (requestedHash == loginUser[0].hash) {
            return loginUser[0].userId
        } else {
            return 0
        }
    }

    @DeleteMapping("/users/delete/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<UsersEntity> {
        val user = repository.findById(id)
        if (user.isEmpty) {
            return ResponseEntity.notFound().build()
        }else {
            repository.deleteById(id)
            return ResponseEntity.ok().build()
        }
    }

}