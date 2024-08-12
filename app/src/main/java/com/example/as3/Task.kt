package com.example.as3

data class Task(
    val name: String,
    var status: TaskStatus = TaskStatus.ACTIVE
)

enum class TaskStatus {
    ACTIVE,
    DONE,
    DELETED
}
