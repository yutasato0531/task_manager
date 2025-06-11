package com.example.task_manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class TestController(
    @Autowired private val repository: TestRepository
) {

    @GetMapping("/test")
    fun get(): List<TestEntity> {
        return repository.findAll().toList()
    }

    @GetMapping("/task/{id}")
    fun getById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val tasks = repository.findById(id)
        if (tasks.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        return ResponseEntity.status(HttpStatus.OK).body(tasks)
    }

    @PostMapping("/test")
    fun postTest(@RequestBody task: TestRequest) {
        val entity = TestEntity(text = task.text)
        repository.save(entity)
    }

    @DeleteMapping("/test/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val tasks = repository.findById(id)
        if (tasks.isEmpty) {
            return ResponseEntity.notFound().build()
        }
        repository.deleteById(id)
        return ResponseEntity.ok().build()
    }

}