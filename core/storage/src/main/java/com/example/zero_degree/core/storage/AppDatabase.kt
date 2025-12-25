package com.example.zero_degree.core.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zero_degree.core.storage.dao.*
import com.example.zero_degree.core.storage.entity.*

@Database(
    entities = [
        BarEntity::class,
        DrinkEntity::class,
        EventEntity::class,
        BookingEntity::class,
        ReviewEntity::class
    ],
    version = 2, // Увеличена версия из-за изменения ID на String
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun barDao(): BarDao
    abstract fun drinkDao(): DrinkDao
    abstract fun eventDao(): EventDao
    abstract fun bookingDao(): BookingDao
    abstract fun reviewDao(): ReviewDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zero_degree_database"
                )
                    .fallbackToDestructiveMigration() // Для разработки - удаляет старую БД
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

