package com.example.task_manager

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TasksRepository : CrudRepository<TasksEntity, Long>