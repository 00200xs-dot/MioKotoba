package com.akira.miokotoba.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * 应用本地数据库入口
 *
 * 这里集中注册所有数据库表和 Dao, Repository 只需要拿到 [wordBookDao] 即可读写数据
 */
@Database(
    entities = [
        WordBookEntity::class,
        WordEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ReviewStateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordBookDao(): WordBookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mio_kotoba_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
