package com.vaultionizer.vaultapp.data.db.dao

import androidx.room.*
import com.vaultionizer.vaultapp.data.db.entity.LocalSpace

@Dao
interface LocalSpaceDao {

    @Query("SELECT * FROM LocalSpace")
    fun getAll(): List<LocalSpace>

    @Query("SELECT * FROM LocalSpace WHERE space_id = :id")
    fun getSpaceById(id: Long): LocalSpace?

    @Query("SELECT * FROM LocalSpace WHERE remote_space_id = :remoteSpaceId AND user_id = :userId")
    fun getSpaceByRemoteId(userId: Long, remoteSpaceId: Long): LocalSpace?

    @Transaction
    @Query("SELECT * FROM LocalSpace")
    fun getSpacesWithFiles(): List<SpaceWithFiles>

    @Transaction
    @Query("SELECT * FROM LocalSpace WHERE space_id = :id")
    fun getSpaceWithFiles(id: Long): SpaceWithFiles

    @Insert
    fun createSpace(localSpace: LocalSpace): Long

    @Update
    fun updateSpaces(vararg space: LocalSpace)

    @Delete
    fun deleteSpaces(vararg spaces: LocalSpace)

}