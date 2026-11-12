package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.HubItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HubDao {
    @Query("SELECT * FROM hub_items ORDER BY orderIndex ASC, id ASC")
    fun getAllItems(): Flow<List<HubItem>>

    @Query("SELECT * FROM hub_items WHERE categoryId = :categoryId ORDER BY orderIndex ASC, id ASC")
    fun getItemsByCategory(categoryId: String): Flow<List<HubItem>>

    @Query("SELECT * FROM hub_items WHERE isFavorite = 1 ORDER BY orderIndex ASC, id ASC")
    fun getFavoriteItems(): Flow<List<HubItem>>

    @Query("SELECT COUNT(*) FROM hub_items")
    suspend fun getItemCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: HubItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HubItem>)

    @Update
    suspend fun updateItem(item: HubItem)

    @Delete
    suspend fun deleteItem(item: HubItem)

    @Query("DELETE FROM hub_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("DELETE FROM hub_items")
    suspend fun deleteAll()
}
