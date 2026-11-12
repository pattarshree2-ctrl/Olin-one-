package com.example.data.repository

import com.example.data.local.DefaultHubData
import com.example.data.local.HubDao
import com.example.data.model.HubItem
import kotlinx.coroutines.flow.Flow

class HubRepository(private val hubDao: HubDao) {
    val allItems: Flow<List<HubItem>> = hubDao.getAllItems()
    val favoriteItems: Flow<List<HubItem>> = hubDao.getFavoriteItems()

    suspend fun ensureInitialized() {
        if (hubDao.getItemCount() == 0) {
            hubDao.insertAll(DefaultHubData.INITIAL_ITEMS)
        }
    }

    suspend fun resetToDefaults() {
        hubDao.deleteAll()
        hubDao.insertAll(DefaultHubData.INITIAL_ITEMS)
    }

    suspend fun insertItem(item: HubItem): Long {
        return hubDao.insertItem(item)
    }

    suspend fun updateItem(item: HubItem) {
        hubDao.updateItem(item)
    }

    suspend fun deleteItem(item: HubItem) {
        hubDao.deleteItem(item)
    }

    suspend fun deleteItemById(id: Long) {
        hubDao.deleteItemById(id)
    }

    suspend fun toggleFavorite(item: HubItem) {
        hubDao.updateItem(item.copy(isFavorite = !item.isFavorite))
    }
}
