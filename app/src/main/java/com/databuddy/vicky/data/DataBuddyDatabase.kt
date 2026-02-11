package com.databuddy.vicky.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlanConfig::class, MonthlyUsage::class],
    version = 1,
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
