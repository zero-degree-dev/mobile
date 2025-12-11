package com.example.zero_degree.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zero_degree.data.local.dao.BarDao
import com.example.zero_degree.data.local.dao.DrinkDao
import com.example.zero_degree.data.local.dao.EventDao
import com.example.zero_degree.data.local.dao.ReviewDao
import com.example.zero_degree.data.local.entity.BarEntity
import com.example.zero_degree.data.local.entity.DrinkEntity
import com.example.zero_degree.data.local.entity.EventEntity
import com.example.zero_degree.data.local.entity.ReviewEntity

@Database(
    entities = [
        DrinkEntity::class,
        BarEntity::class,
        EventEntity::class,
        ReviewEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun drinkDao(): DrinkDao
    abstract fun barDao(): BarDao
    abstract fun eventDao(): EventDao
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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

