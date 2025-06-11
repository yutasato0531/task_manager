package com.example.task_manager

import com.fasterxml.jackson.annotation.Nulls
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import kotlin.jvm.optionals.toList

@RestController
class UsersController(
    @Autowired private val repository: UsersRepository
) {
    @GetMapping("/users/get")
    fun getUser(): List<UsersEntity> {
        return repository.findAll().toList()
    }

    @GetMapping("/users/get/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<UsersEntity> {
        val user = repository.findById(id)
        return if (user.isPresent) {
            ResponseEntity.ok(user.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/users/post")
    fun postUser(@RequestBody user: UserRegRequest): String {
        fun getRandomString(length: Int) : String {
            val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return (1..length)
                .map { charset.random() }
                .joinToString("")
        }

        val salt = getRandomString(10)

        fun generateSha256Hash(input: String): String {
            val bytes = input.toByteArray(Charsets.UTF_8)
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)

            return digest.fold("") { str, it -> str + "%02x".format(it) }
        }

        val hash = generateSha256Hash(user.pass + salt)

        val entity = UsersEntity(
            userName = user.userName, firstName = user.firstName, lastName = user.lastName, salt = salt,  hash = hash)
        repository.save(entity)
        return ""
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