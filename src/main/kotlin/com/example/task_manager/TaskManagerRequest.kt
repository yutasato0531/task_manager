package com.example.task_manager

import java.sql.Timestamp
import java.time.Year

data class TasksRequest(val userId: Long?, val task: String, val year: Int, val month: Int, val date: Int, val hour: Int, val minute: Int)
data class UserRegRequest(val userName: String, val firstName: String, var lastName: String,val pass: String)
data class TestRequest(val text: String)
data class LoginRequest(val userName: String, val password: String)
data class SessionsRequest(val sessionId: String, val userId: Long, val userName: String)