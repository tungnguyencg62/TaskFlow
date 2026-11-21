package online.futuret.taskflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.futuret.taskflow.data.local.dao.TaskDao
import online.futuret.taskflow.data.local.entity.ProjectEntity
import online.futuret.taskflow.data.local.entity.TaskEntity


@Database(
    entities = [TaskEntity::class, ProjectEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "taskflow_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}