package com.example.studybuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session

@Database(
    entities = [Subjects::class, Task::class, session::class],
    version = 1
)

@TypeConverters(ColorListConverter::class)
abstract class AppDatabase: RoomDatabase() {


    abstract fun subjectDao(): SubjectDAO
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao



}