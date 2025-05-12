package com.example.studybuddy.data.repo

import com.example.studybuddy.data.local.TaskDao
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl@Inject constructor(

    private val taskDao: TaskDao

): TaskRepository {

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun getUpcomingTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllUpcomingTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
            //return all the tasks which are incomplete
            .map { tasks -> tasks.filter { it.isCompleted.not() } }
            .map { tasks -> sortTasks(tasks) }
    }
    //create the list in sorted order
    private fun sortTasks(tasks : List<Task>) : List<Task>{
        return tasks.sortedWith(compareBy<Task>{it.dueDate}.thenByDescending { it.priority })
    }
}