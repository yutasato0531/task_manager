package com.example.task_manager

import com.fasterxml.jackson.annotation.Nulls
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import kotlin.jvm.optionals.toList


@RestController
class TasksController(
    @Autowired private val repository: TasksRepository
) {
    @GetMapping("/tasks")
    fun getTask(): List<TasksEntity> {
        return repository.findAll().toList()
    }

    @GetMapping("/tasks/{id}")
    fun getTaskById(@PathVariable("id") id: Long): ResponseEntity<TasksEntity> {
        val task = repository.findById(id)
        return if (task.isPresent) {
            ResponseEntity.ok(task.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/tasks")
    fun postTask(@RequestBody task: TasksRequest): String {
        val entity = TasksEntity(task = task.task, year = task.year  , month = task.month,  date = task.date, hour = task.hour, minute = task.minute)
        repository.save(entity)
        return ""
    }

    @DeleteMapping("/tasks/{id}")
    fun deleteTaskById(@PathVariable("id") id: Long): ResponseEntity<TasksEntity> {
        val task = repository.findById(id)
        if (task.isEmpty) {
            return ResponseEntity.notFound().build()
        }else {
            repository.deleteById(id)
            return ResponseEntity.ok().build()
        }
    }

}