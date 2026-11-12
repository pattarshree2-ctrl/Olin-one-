package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Categories
import com.example.data.model.CategoryInfo
import com.example.data.model.LanguageMode
import com.example.ui.theme.HubCyanAccent
import com.example.ui.theme.HubCyanAccentDark
import com.example.ui.theme.HubSurfaceDark
import com.example.ui.theme.HubSurfaceElevated
import com.example.ui.theme.HubTextMuted
import com.example.ui.theme.HubTextPrimary
import com.example.ui.theme.HubTextSecondary

@Composable
fun HubHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    categories: List<CategoryInfo>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    languageMode: LanguageMode,
    onToggleLanguage: () -> Unit,
    onResetDefaults: () -> Unit,
    totalResultCount: Int,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(HubSurfaceDark)
            .padding(top = 10.dp, bottom = 8.dp)
    ) {
        // Top row: App title + language button + menu
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🚀 AI & Social Hub",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = HubCyanAccent
                )
                Text(
                    text = when (languageMode) {
                        LanguageMode.KANNADA -> "ನಿಮ್ಮ ಎಲ್ಲಾ ಪ್ರಮುಖ AI ಮತ್ತು ಸೋಶಿಯಲ್ ಆಪ್ಗಳು ಒಂದೇ ಸ್ಥಳದಲ್ಲಿ"
                        LanguageMode.ENGLISH -> "All essential AI tools and social apps in one place"
                        LanguageMode.DUAL -> "ನಿಮ್ಮ ಪ್ರಮುಖ AI & ಸೋಶಿಯಲ್ ಆಪ್ಗಳು (All-in-one Hub)"
                    },
                    fontSize = 11.5.sp,
                    color = HubTextSecondary,
                    maxLines = 1
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Language Mode Toggle Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(HubSurfaceElevated)
                        .border(1.dp, HubCyanAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .clickable { onToggleLanguage() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("lang_toggle_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Change Language",
                            tint = HubCyanAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = languageMode.shortLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = HubCyanAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Overflow Menu
                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_menu_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = HubTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Reset to Default Links (ಮರುಹೊಂದಿಸಿ)") },
                            leadingIcon = {
                                Icon(Icons.Default.RestartAlt, contentDescription = null, tint = HubCyanAccent)
                            },
                            onClick = {
                                menuExpanded = false
                                onResetDefaults()
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Prominent Search Bar at top of screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = if (languageMode == LanguageMode.KANNADA) {
                            "ಶೀರ್ಷಿಕೆ ಹುಡುಕಿ (Gemini, Facebook, Video, ಇಮೇಜ್)..."
                        } else {
                            "Search categories & websites by title..."
                        },
                        fontSize = 13.sp,
                        color = HubTextMuted
                    )
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = HubCyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.testTag("clear_search_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = HubCyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = HubTextPrimary,
                    unfocusedTextColor = HubTextPrimary,
                    focusedContainerColor = HubSurfaceElevated,
                    unfocusedContainerColor = HubSurfaceElevated,
                    focusedBorderColor = HubCyanAccent,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("top_search_bar")
            )
        }

        // Active search status indicator
        AnimatedVisibility(
            visible = searchQuery.isNotBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (languageMode == LanguageMode.KANNADA) {
                        "ಶೋಧನೆ ಫಲಿತಾಂಶ: $totalResultCount ವೆಬ್‌ಸೈಟ್‌ಗಳು (${categories.filter { it.id != Categories.ALL.id }.size} ವಿಭಾಗಗಳು)"
                    } else {
                        "Filtering: $totalResultCount website(s) across ${categories.filter { it.id != Categories.ALL.id }.size} category/categories"
                    },
                    fontSize = 11.5.sp,
                    color = HubCyanAccent,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Clear",
                    fontSize = 11.5.sp,
                    color = HubTextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onSearchQueryChange("") }
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal Category Tabs (filtered dynamically based on search query)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories, key = { it.id }) { category ->
                val isSelected = selectedCategory == category.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectCategory(category.id) },
                    label = {
                        Text(
                            text = "${category.emoji} ${when (languageMode) {
                                LanguageMode.KANNADA -> category.titleKn
                                LanguageMode.ENGLISH -> category.titleEn
                                LanguageMode.DUAL -> category.titleEn
                            }}",
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HubCyanAccent,
                        selectedLabelColor = HubSurfaceDark,
                        containerColor = HubSurfaceElevated,
                        labelColor = HubTextPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.outline,
                        selectedBorderColor = HubCyanAccent
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.testTag("category_chip_${category.id}")
                )
            }
        }
    }
}
