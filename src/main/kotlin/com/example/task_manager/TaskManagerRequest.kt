package com.example.task_manager

import java.time.Year

data class TasksRequest(val task: String, val year: Int, val month: Int, val date: Int, val hour: Int, val minute: Int)
data class UserRegRequest(val userName: String, val firstName: String, var lastName: String,val pass: String)
data class TestRequest(val text: String)