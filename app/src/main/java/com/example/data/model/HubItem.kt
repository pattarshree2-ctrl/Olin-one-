package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hub_items")
data class HubItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val nameKn: String? = null,
    val url: String,
    val categoryId: String,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val iconEmoji: String = "🌐",
    val description: String = "",
    val orderIndex: Int = 0
)

enum class LanguageMode(val label: String, val shortLabel: String) {
    DUAL("Dual (ಕನ್ನಡ / English)", "ಕನ್ನಡ + EN"),
    KANNADA("ಕನ್ನಡ (Kannada)", "ಕನ್ನಡ"),
    ENGLISH("English", "EN")
}
