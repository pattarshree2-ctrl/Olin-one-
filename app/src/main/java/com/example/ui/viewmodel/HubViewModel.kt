package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Categories
import com.example.data.model.CategoryInfo
import com.example.data.model.HubItem
import com.example.data.model.LanguageMode
import com.example.data.repository.HubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AddEditDialogState(
    val isOpen: Boolean = false,
    val itemToEdit: HubItem? = null,
    val initialCategoryId: String = Categories.CUSTOM.id
)

class HubViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HubRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = HubRepository(database.hubDao())
        viewModelScope.launch {
            repository.ensureInitialized()
        }
    }

    val allItems: StateFlow<List<HubItem>> = repository.allItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _languageMode = MutableStateFlow(LanguageMode.DUAL)
    val languageMode: StateFlow<LanguageMode> = _languageMode.asStateFlow()

    // Browser state: null means home hub, non-null means in-app browser is showing this URL
    private val _browserUrl = MutableStateFlow<String?>(null)
    val browserUrl: StateFlow<String?> = _browserUrl.asStateFlow()

    private val _browserTitle = MutableStateFlow<String?>(null)
    val browserTitle: StateFlow<String?> = _browserTitle.asStateFlow()

    // Dialog state
    private val _dialogState = MutableStateFlow(AddEditDialogState())
    val dialogState: StateFlow<AddEditDialogState> = _dialogState.asStateFlow()

    // Filtered categories based on search query matching category titles or card titles
    val filteredCategories: StateFlow<List<CategoryInfo>> = combine(
        allItems,
        _searchQuery
    ) { items, query ->
        if (query.isBlank()) {
            Categories.FILTER_TABS
        } else {
            val q = query.trim().lowercase()
            val matchingSections = Categories.SECTIONS.filter { cat ->
                val catTitleMatches = cat.titleEn.lowercase().contains(q) || cat.titleKn.lowercase().contains(q)
                val hasMatchingItem = items.any { item ->
                    item.categoryId == cat.id && (
                        item.name.lowercase().contains(q) ||
                        (item.nameKn?.lowercase()?.contains(q) == true)
                    )
                }
                catTitleMatches || hasMatchingItem
            }

            val hasMatchingFav = items.any { item ->
                item.isFavorite && (
                    item.name.lowercase().contains(q) ||
                    (item.nameKn?.lowercase()?.contains(q) == true)
                )
            }

            val result = mutableListOf(Categories.ALL)
            if (hasMatchingFav) {
                result.add(Categories.FAVORITES)
            }
            result.addAll(matchingSections)
            result
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Categories.FILTER_TABS
    )

    // Filtered items flow based on search query and selected category
    val filteredItems: StateFlow<List<HubItem>> = combine(
        allItems,
        _selectedCategory,
        _searchQuery
    ) { items, category, query ->
        var list = items

        // Filter by search query on titles (category titles and card titles)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter { item ->
                val cat = Categories.getCategory(item.categoryId)
                val catMatches = cat.titleEn.lowercase().contains(q) || cat.titleKn.lowercase().contains(q)
                val itemTitleMatches = item.name.lowercase().contains(q) ||
                    (item.nameKn?.lowercase()?.contains(q) == true)

                catMatches || itemTitleMatches
            }
        }

        // Filter by selected category chip
        if (category == Categories.FAVORITES.id) {
            list = list.filter { it.isFavorite }
        } else if (category != Categories.ALL.id) {
            list = list.filter { it.categoryId == category }
        }

        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectCategory(categoryId: String) {
        _selectedCategory.value = categoryId
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isNotBlank() && _selectedCategory.value != Categories.ALL.id) {
            val q = query.trim().lowercase()
            val cat = Categories.getCategory(_selectedCategory.value)
            val catMatches = cat.titleEn.lowercase().contains(q) || cat.titleKn.lowercase().contains(q)
            val itemsInCatMatch = allItems.value.any { item ->
                item.categoryId == cat.id && (
                    item.name.lowercase().contains(q) || (item.nameKn?.lowercase()?.contains(q) == true)
                )
            }
            if (!catMatches && !itemsInCatMatch) {
                _selectedCategory.value = Categories.ALL.id
            }
        }
    }

    fun setLanguageMode(mode: LanguageMode) {
        _languageMode.value = mode
    }

    fun toggleLanguageMode() {
        _languageMode.value = when (_languageMode.value) {
            LanguageMode.DUAL -> LanguageMode.KANNADA
            LanguageMode.KANNADA -> LanguageMode.ENGLISH
            LanguageMode.ENGLISH -> LanguageMode.DUAL
        }
    }

    fun openBrowser(url: String, title: String? = null) {
        val sanitized = if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            "https://$url"
        }
        _browserUrl.value = sanitized
        _browserTitle.value = title
    }

    fun closeBrowser() {
        _browserUrl.value = null
        _browserTitle.value = null
    }

    fun openAddDialog(categoryId: String = Categories.CUSTOM.id) {
        _dialogState.value = AddEditDialogState(
            isOpen = true,
            itemToEdit = null,
            initialCategoryId = categoryId
        )
    }

    fun openEditDialog(item: HubItem) {
        _dialogState.value = AddEditDialogState(
            isOpen = true,
            itemToEdit = item,
            initialCategoryId = item.categoryId
        )
    }

    fun closeDialog() {
        _dialogState.value = AddEditDialogState(isOpen = false)
    }

    fun saveWebsite(name: String, nameKn: String?, url: String, categoryId: String, emoji: String) {
        viewModelScope.launch {
            val formattedUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
                url.trim()
            } else {
                "https://${url.trim()}"
            }

            val currentEdit = _dialogState.value.itemToEdit
            if (currentEdit != null) {
                repository.updateItem(
                    currentEdit.copy(
                        name = name.trim(),
                        nameKn = nameKn?.trim().takeIf { !it.isNullOrBlank() },
                        url = formattedUrl,
                        categoryId = categoryId,
                        iconEmoji = emoji
                    )
                )
            } else {
                val newItem = HubItem(
                    name = name.trim(),
                    nameKn = nameKn?.trim().takeIf { !it.isNullOrBlank() },
                    url = formattedUrl,
                    categoryId = categoryId,
                    isCustom = true,
                    iconEmoji = emoji,
                    description = "Custom link"
                )
                repository.insertItem(newItem)
            }
            closeDialog()
        }
    }

    fun toggleFavorite(item: HubItem) {
        viewModelScope.launch {
            repository.toggleFavorite(item)
        }
    }

    fun deleteItem(item: HubItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun resetDefaults() {
        viewModelScope.launch {
            repository.resetToDefaults()
        }
    }
}
