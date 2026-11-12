package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Categories
import com.example.data.model.HubItem
import com.example.data.model.LanguageMode
import com.example.ui.theme.HubCyanAccent
import com.example.ui.theme.HubGreenSuccess
import com.example.ui.theme.HubRedError
import com.example.ui.theme.HubSurfaceDark
import com.example.ui.theme.HubSurfaceElevated
import com.example.ui.theme.HubTextMuted
import com.example.ui.theme.HubTextPrimary
import com.example.ui.theme.HubTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditWebsiteDialog(
    itemToEdit: HubItem?,
    initialCategoryId: String,
    languageMode: LanguageMode,
    onDismiss: () -> Unit,
    onSave: (name: String, nameKn: String?, url: String, categoryId: String, emoji: String) -> Unit
) {
    var name by remember { mutableStateOf(itemToEdit?.name ?: "") }
    var nameKn by remember { mutableStateOf(itemToEdit?.nameKn ?: "") }
    var url by remember { mutableStateOf(itemToEdit?.url ?: "") }
    var selectedCategoryId by remember { mutableStateOf(itemToEdit?.categoryId ?: initialCategoryId) }
    var selectedEmoji by remember { mutableStateOf(itemToEdit?.iconEmoji ?: "🌐") }

    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var urlError by remember { mutableStateOf(false) }

    val emojis = listOf("🌐", "🤖", "🎨", "🎬", "🔄", "🎵", "📂", "✨", "🧠", "⚡", "🚀", "💬", "📸", "💻")

    val isEditing = itemToEdit != null
    val dialogTitle = when (languageMode) {
        LanguageMode.KANNADA -> if (isEditing) "ವೆಬ್‌ಸೈಟ್ ಸಂಪಾದಿಸಿ" else "ಹೊಸ ವೆಬ್ಸೈಟ್ ಸೇರಿಸಿ"
        LanguageMode.ENGLISH -> if (isEditing) "Edit Website" else "Add New Website"
        LanguageMode.DUAL -> if (isEditing) "ವೆಬ್‌ಸೈಟ್ ಸಂಪಾದಿಸಿ (Edit Website)" else "ಹೊಸ ವೆಬ್ಸೈಟ್ ಸೇರಿಸಿ (Add Website)"
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = HubSurfaceDark,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Text(
                    text = dialogTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = HubCyanAccent,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) nameError = false
                    },
                    label = { Text("ವೆಬ್ಸೈಟ್ ಹೆಸರು / Website Name") },
                    placeholder = { Text("ಉದಾ: Pika Labs, Canva") },
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text("ಹೆಸರು ಅಗತ್ಯವಿದೆ / Name is required", color = HubRedError) }
                    } else null,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HubTextPrimary,
                        unfocusedTextColor = HubTextPrimary,
                        focusedBorderColor = HubCyanAccent,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("site_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Optional Kannada name
                OutlinedTextField(
                    value = nameKn,
                    onValueChange = { nameKn = it },
                    label = { Text("ಕನ್ನಡ ಹೆಸರು (ಐಚ್ಛಿಕ) / Kannada Name (Opt)") },
                    placeholder = { Text("ಉದಾ: ಪಿಕಾ ಲ್ಯಾಬ್ಸ್") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HubTextPrimary,
                        unfocusedTextColor = HubTextPrimary,
                        focusedBorderColor = HubCyanAccent,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("site_name_kn_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // URL field
                OutlinedTextField(
                    value = url,
                    onValueChange = {
                        url = it
                        if (it.isNotBlank()) urlError = false
                    },
                    label = { Text("ವೆಬ್ಸೈಟ್ ಲಿಂಕ್ / Website Link") },
                    placeholder = { Text("https://example.com") },
                    isError = urlError,
                    supportingText = if (urlError) {
                        { Text("ಲಿಂಕ್ ಅಗತ್ಯವಿದೆ / URL is required", color = HubRedError) }
                    } else null,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HubTextPrimary,
                        unfocusedTextColor = HubTextPrimary,
                        focusedBorderColor = HubCyanAccent,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("site_url_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Selector
                Text(
                    text = "ವಿಭಾಗ / Category",
                    fontSize = 13.sp,
                    color = HubTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    val currentCat = Categories.getCategory(selectedCategoryId)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(HubSurfaceElevated)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                            .clickable { categoryMenuExpanded = true }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${currentCat.emoji} ${currentCat.titleEn} (${currentCat.titleKn})",
                            color = HubTextPrimary,
                            fontSize = 14.sp
                        )
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select category",
                            tint = HubCyanAccent
                        )
                    }

                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false }
                    ) {
                        Categories.SECTIONS.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.emoji} ${cat.titleEn} / ${cat.titleKn}") },
                                onClick = {
                                    selectedCategoryId = cat.id
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Emoji Picker
                Text(
                    text = "ಚಿಹ್ನೆ / Icon Emoji",
                    fontSize = 13.sp,
                    color = HubTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    emojis.forEach { emoji ->
                        val isSelected = selectedEmoji == emoji
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) HubCyanAccent.copy(alpha = 0.25f) else HubSurfaceElevated
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) HubCyanAccent else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions: Cancel & Save
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = HubRedError
                        ),
                        modifier = Modifier.testTag("cancel_website_btn")
                    ) {
                        Text(text = if (languageMode == LanguageMode.KANNADA) "ರದ್ದುಮಾಡಿ" else "Cancel")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            var valid = true
                            if (name.isBlank()) {
                                nameError = true
                                valid = false
                            }
                            if (url.isBlank()) {
                                urlError = true
                                valid = false
                            }
                            if (valid) {
                                onSave(name, nameKn, url, selectedCategoryId, selectedEmoji)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HubGreenSuccess,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("save_website_btn")
                    ) {
                        Text(
                            text = if (languageMode == LanguageMode.KANNADA) "ಸೇರಿಸಿ" else if (isEditing) "Update" else "Save",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
