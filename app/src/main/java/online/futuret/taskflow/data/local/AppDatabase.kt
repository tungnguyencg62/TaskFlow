package online.futuret.taskflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.futuret.taskflow.data.local.dao.BoardDao
import online.futuret.taskflow.data.local.dao.ColumnDao
import online.futuret.taskflow.data.local.dao.ProjectDao
import online.futuret.taskflow.data.local.dao.TaskDao
import online.futuret.taskflow.data.local.entity.BoardEntity
import online.futuret.taskflow.data.local.entity.ColumnEntity
import online.futuret.taskflow.data.local.entity.ProjectEntity
import online.futuret.taskflow.data.local.entity.TaskEntity


@Database(
    entities = [TaskEntity::class,
                ProjectEntity::class,
               BoardEntity::class,
               ColumnEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun projectDao(): ProjectDao

    abstract fun boardDao(): BoardDao

    abstract fun columnDao(): ColumnDao

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