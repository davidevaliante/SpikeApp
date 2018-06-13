package com.musashi.claymore.spike.spike

import android.arch.persistence.room.*
import android.content.Context
import io.reactivex.Single


@Dao
interface DetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(vararg detailedDescription: DetailedDescription)

    @Update
    fun updateUsers(vararg users: DetailedDescription)

    @Delete
    fun deleteUsers(vararg users: DetailedDescription)

    @Query("SELECT * FROM SlotMachines")
    fun loadAllUsers(): List<DetailedDescription>

    @Query("SELECT * FROM SlotMachines WHERE shortName LIKE :name")
    fun findSlotWithName(name: String): List<DetailedDescription>
}

@Database(entities = arrayOf(DetailedDescription::class), version = 1)
abstract class SpikeDatabase : RoomDatabase() {

    abstract fun detailsDataDao(): DetailsDao

    companion object {
        private var INSTANCE: SpikeDatabase? = null

        fun getInstance(context: Context): SpikeDatabase? {
            if (INSTANCE == null) {
                synchronized(SpikeDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            SpikeDatabase::class.java, "weather.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}