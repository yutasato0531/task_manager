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

    @GetMapping("/tasks/task_id/{task_id}")
    fun getTaskByTaskId(@PathVariable("task_id") taskId: Long): ResponseEntity<TasksEntity> {
        val task = repository.findById(taskId)
        return if (task.isPresent) {
            ResponseEntity.ok(task.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/tasks/user_id/{user_id}")
    fun getTaskByUserId(@PathVariable("user_id") userId: Long): List<TasksEntity> {
        val tasks = repository.findAll().filter { it.userId == userId }.toList()
        return tasks
    }

    @PostMapping("/tasks")
    fun postTask(@RequestBody task: TasksRequest): String {
        val entity = TasksEntity(userId = task.userId, task = task.task, year = task.year  , month = task.month,  date = task.date, hour = task.hour, minute = task.minute)
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