package com.example.androidcleanarchitecture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.SATScoresDao
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.model.SchoolDao
import java.util.concurrent.Executors


@Database(entities = [School::class, SATScores::class], version = 1, exportSchema = false)
abstract class SchoolRoomDatabase : RoomDatabase() {

    abstract val schoolDao: SchoolDao
    abstract val satScoresDao: SATScoresDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolRoomDatabase? = null

        private const val NUMBER_OF_THREADS = 10

        // Uses the Executer Service to run DB operations in background and concurrently
        val databaseWriteExecutor = Executors.newFixedThreadPool(
            NUMBER_OF_THREADS
        )

        /**
         * DB Singleton
         * @param context
         * @return
         */
        @Synchronized
        fun getDatabaseInstance(context: Context): SchoolRoomDatabase {
            if (INSTANCE == null) {
                synchronized(SchoolRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SchoolRoomDatabase::class.java, "school_database"
                        ).fallbackToDestructiveMigration()
                          //  .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

        /**
         * Handle any setup after DB is created for the first time
         */
        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    val schoolDao: SchoolDao = INSTANCE!!.schoolDao
                 //   schoolDao.deleteAll()
                    val scoresDao: SATScoresDao =
                        INSTANCE!!.satScoresDao
                   // scoresDao.deleteAll()
                }
            }
        }
    }
}
