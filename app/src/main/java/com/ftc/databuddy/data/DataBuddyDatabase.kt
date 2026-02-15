package com.ftc.databuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlanConfig::class, MonthlyUsage::class],
    version = 5,
    exportSchema = false
)
abstract class DataBuddyDatabase : RoomDatabase() {
    
    abstract fun dataBuddyDao(): DataBuddyDao
    
    companion object {
        @Volatile
        private var INSTANCE: DataBuddyDatabase? = null
        
        fun getDatabase(context: Context): DataBuddyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBuddyDatabase::class.java,
                    "data_buddy_database"
                )
                    .fallbackToDestructiveMigration() // Clear old data on schema change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
