package com.example.task_manager

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Primary
import java.time.Month
import java.time.Year

@Entity
@Table(name = "test")
class TestEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    var text: String,
)

@Entity
@Table(name = "users")
class UsersEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,
    var userName: String? = null,
    var firstName: String,
    var lastName: String,
    var salt: String? = null,
    var hash: String? = null,
)

@Entity
@Table(name = "tasks")
class TasksEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var taskId: Long? = null,
    var userId: Long? = null,
    var task: String,
    var year: Int?=null,
    var month: Int?=null,
    var date: Int?=null,
    var hour: Int?=null,
    var minute: Int?=null,
)

@Entity
@Table(name = "tasks")
class SessionsEntity(
    @Id
    var sessionId: Long? = null,
    var timeStamp: Long? = null,
    var userId: Long? = null,
)