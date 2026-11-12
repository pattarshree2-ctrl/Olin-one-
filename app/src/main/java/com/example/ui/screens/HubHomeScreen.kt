package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Categories
import com.example.data.model.CategoryInfo
import com.example.data.model.HubItem
import com.example.data.model.LanguageMode
import com.example.ui.components.AddEditWebsiteDialog
import com.example.ui.components.HubCard
import com.example.ui.components.HubHeader
import com.example.ui.components.InAppBrowserScreen
import com.example.ui.theme.HubCyanAccent
import com.example.ui.theme.HubCyanAccentDark
import com.example.ui.theme.HubDarkBg
import com.example.ui.theme.HubRedError
import com.example.ui.theme.HubSurfaceDark
import com.example.ui.theme.HubSurfaceElevated
import com.example.ui.theme.HubTextMuted
import com.example.ui.theme.HubTextPrimary
import com.example.ui.theme.HubTextSecondary
import com.example.ui.viewmodel.HubViewModel

@Composable
fun HubHomeScreen(
    viewModel: HubViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allItems by viewModel.allItems.collectAsStateWithLifecycle()
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle()
    val filteredCategories by viewModel.filteredCategories.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val languageMode by viewModel.languageMode.collectAsStateWithLifecycle()
    val browserUrl by viewModel.browserUrl.collectAsStateWithLifecycle()
    val browserTitle by viewModel.browserTitle.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    var itemToDelete by remember { mutableStateOf<HubItem?>(null) }
    var showResetDialog by remember { mutableStateOf(false) }

    // If browser is active, display full-screen in-app WebView
    if (browserUrl != null) {
        InAppBrowserScreen(
            initialUrl = browserUrl!!,
            initialTitle = browserTitle,
            onClose = { viewModel.closeBrowser() }
        )
        return
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(HubDarkBg),
        topBar = {
            HubHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                categories = filteredCategories,
                selectedCategory = selectedCategory,
                onSelectCategory = { viewModel.selectCategory(it) },
                languageMode = languageMode,
                onToggleLanguage = { viewModel.toggleLanguageMode() },
                onResetDefaults = { showResetDialog = true },
                totalResultCount = filteredItems.size,
                modifier = Modifier.statusBarsPadding()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.openAddDialog(
                        selectedCategory.takeIf { it != Categories.ALL.id && it != Categories.FAVORITES.id } ?: Categories.CUSTOM.id
                    )
                },
                containerColor = HubCyanAccentDark,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("fab_add_website")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Website")
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = if (languageMode == LanguageMode.KANNADA) "ವೆಬ್‌ಸೈಟ್ ಸೇರಿಸಿ" else "+ Add Website",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(HubDarkBg)
                .padding(innerPadding)
        ) {
            val columns = if (maxWidth > 900.dp) 4 else if (maxWidth > 600.dp) 3 else 2

            if (filteredItems.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = HubTextMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (languageMode == LanguageMode.KANNADA) {
                            if (searchQuery.isNotBlank()) "'$searchQuery' ಗೆ ಯಾವುದೇ ಫಲಿತಾಂಶವಿಲ್ಲ" else "ಯಾವುದೇ ವೆಬ್‌ಸೈಟ್‌ಗಳು ಕಂಡುಬಂದಿಲ್ಲ"
                        } else {
                            if (searchQuery.isNotBlank()) "No categories or websites matching '$searchQuery'" else "No websites found"
                        },
                        color = HubTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "Check your spelling or try searching another title" else "Click '+ Add' to save your favorite link",
                        color = HubTextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    if (searchQuery.isNotBlank()) {
                        Button(
                            onClick = { viewModel.onSearchQueryChange("") },
                            colors = ButtonDefaults.buttonColors(containerColor = HubCyanAccentDark)
                        ) {
                            Text("Clear Search (ಎಲ್ಲವನ್ನೂ ತೋರಿಸಿ)")
                        }
                    } else {
                        Button(
                            onClick = { viewModel.openAddDialog(selectedCategory) },
                            colors = ButtonDefaults.buttonColors(containerColor = HubCyanAccentDark)
                        ) {
                            Text("+ Add First Website")
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Check if viewing "All" (either searching or not searching): group into matching category sections!
                    if (selectedCategory == Categories.ALL.id) {
                        // Show Favorites Section if any matching favorites exist and search query is blank
                        val favs = filteredItems.filter { it.isFavorite }
                        if (favs.isNotEmpty() && searchQuery.isBlank()) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                SectionHeader(
                                    category = Categories.FAVORITES,
                                    languageMode = languageMode,
                                    onAddClick = null
                                )
                            }
                            items(favs, key = { "fav_${it.id}" }) { item ->
                                HubCard(
                                    item = item,
                                    languageMode = languageMode,
                                    onOpenInBrowser = {
                                        viewModel.openBrowser(it.url, it.name)
                                    },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onEdit = { viewModel.openEditDialog(it) },
                                    onDelete = { itemToDelete = it }
                                )
                            }
                        }

                        // Display matching Category sections
                        val sectionsToShow = Categories.SECTIONS.filter { cat ->
                            filteredCategories.any { it.id == cat.id }
                        }

                        sectionsToShow.forEach { cat ->
                            val sectionItems = filteredItems.filter { it.categoryId == cat.id }
                            if (sectionItems.isNotEmpty() || (cat == Categories.CUSTOM && searchQuery.isBlank())) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    SectionHeader(
                                        category = cat,
                                        languageMode = languageMode,
                                        onAddClick = { viewModel.openAddDialog(cat.id) }
                                    )
                                }

                                if (sectionItems.isEmpty()) {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(HubSurfaceDark)
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (languageMode == LanguageMode.KANNADA) "ಯಾವುದೇ ಕಸ್ಟಮ್ ಲಿಂಕ್‌ಗಳಿಲ್ಲ. ಹೊಸದನ್ನು ಸೇರಿಸಲು '+ Add' ಕ್ಲಿಕ್ ಮಾಡಿ" else "No custom links added yet. Tap '+ Add' to save your own!",
                                                color = HubTextMuted,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    items(sectionItems, key = { it.id }) { item ->
                                        HubCard(
                                            item = item,
                                            languageMode = languageMode,
                                            onOpenInBrowser = {
                                                viewModel.openBrowser(it.url, it.name)
                                            },
                                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                                            onEdit = { viewModel.openEditDialog(it) },
                                            onDelete = { itemToDelete = it }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // User selected a specific category tab (e.g. Social, AI Assistant, Favorites, etc.)
                        val activeCat = Categories.getCategory(selectedCategory)
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            SectionHeader(
                                category = activeCat,
                                languageMode = languageMode,
                                onAddClick = { viewModel.openAddDialog(selectedCategory) }
                            )
                        }

                        items(filteredItems, key = { it.id }) { item ->
                            HubCard(
                                item = item,
                                languageMode = languageMode,
                                onOpenInBrowser = {
                                    viewModel.openBrowser(it.url, it.name)
                                },
                                onToggleFavorite = { viewModel.toggleFavorite(it) },
                                onEdit = { viewModel.openEditDialog(it) },
                                onDelete = { itemToDelete = it }
                            )
                        }
                    }

                    // Footer attribution
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 28.dp, bottom = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Designed for Shreenivas Sonar | All-In-One Platform",
                                color = HubTextMuted,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🚀 AI & Social Hub • v1.0",
                                color = HubCyanAccent.copy(alpha = 0.6f),
                                fontSize = 10.5.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Dialog
    if (dialogState.isOpen) {
        AddEditWebsiteDialog(
            itemToEdit = dialogState.itemToEdit,
            initialCategoryId = dialogState.initialCategoryId,
            languageMode = languageMode,
            onDismiss = { viewModel.closeDialog() },
            onSave = { name, nameKn, url, categoryId, emoji ->
                viewModel.saveWebsite(name, nameKn, url, categoryId, emoji)
                Toast.makeText(context, "Website saved successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Delete Confirmation Dialog
    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = {
                Text(
                    text = if (languageMode == LanguageMode.KANNADA) "ವೆಬ್‌ಸೈಟ್ ಅಳಿಸಬೇಕೆ?" else "Delete Website?",
                    color = HubTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete '${itemToDelete?.name}'?",
                    color = HubTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDelete?.let { viewModel.deleteItem(it) }
                        itemToDelete = null
                        Toast.makeText(context, "Website deleted", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HubRedError)
                ) {
                    Text(if (languageMode == LanguageMode.KANNADA) "ಅಳಿಸಿ" else "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text(if (languageMode == LanguageMode.KANNADA) "ರದ್ದುಮಾಡಿ" else "Cancel", color = HubTextPrimary)
                }
            },
            containerColor = HubSurfaceDark
        )
    }

    // Reset Defaults Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = if (languageMode == LanguageMode.KANNADA) "ಮರುಹೊಂದಿಸಬೇಕೆ?" else "Reset to Defaults?",
                    color = HubTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will restore all default AI tools and Social Media links. Any custom links will be removed.",
                    color = HubTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetDefaults()
                        showResetDialog = false
                        Toast.makeText(context, "Defaults restored!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HubCyanAccentDark)
                ) {
                    Text("Restore Defaults")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = HubTextPrimary)
                }
            },
            containerColor = HubSurfaceDark
        )
    }
}

@Composable
fun SectionHeader(
    category: CategoryInfo,
    languageMode: LanguageMode,
    onAddClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val title = when (languageMode) {
        LanguageMode.KANNADA -> "${category.emoji} ${category.titleKn}"
        LanguageMode.ENGLISH -> "${category.emoji} ${category.titleEn}"
        LanguageMode.DUAL -> "${category.emoji} ${category.titleKn} (${category.titleEn})"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = HubCyanAccent,
            modifier = Modifier.weight(1f)
        )

        if (onAddClick != null) {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HubCyanAccentDark,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(28.dp)
                    .testTag("add_btn_section_${category.id}")
            ) {
                Text(
                    text = "+ Add",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
