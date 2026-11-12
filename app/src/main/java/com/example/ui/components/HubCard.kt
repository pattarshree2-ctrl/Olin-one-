package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HubItem
import com.example.data.model.LanguageMode
import com.example.ui.theme.HubCyanAccent
import com.example.ui.theme.HubPinkAccent
import com.example.ui.theme.HubSurfaceDark
import com.example.ui.theme.HubTextMuted
import com.example.ui.theme.HubTextPrimary
import com.example.ui.theme.HubTextSecondary

@Composable
fun HubCard(
    item: HubItem,
    languageMode: LanguageMode,
    onOpenInBrowser: (HubItem) -> Unit,
    onToggleFavorite: (HubItem) -> Unit,
    onEdit: (HubItem) -> Unit,
    onDelete: (HubItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isPressed) HubCyanAccent else MaterialTheme.colorScheme.outline,
        label = "border_color"
    )

    // Extract clean display domain
    val displayHost = remember(item.url) {
        try {
            val uri = Uri.parse(item.url)
            uri.host?.removePrefix("www.") ?: item.url
        } catch (_: Exception) {
            item.url
        }
    }

    // Determine title text based on language mode
    val primaryTitle = when (languageMode) {
        LanguageMode.KANNADA -> item.nameKn ?: item.name
        LanguageMode.ENGLISH -> item.name
        LanguageMode.DUAL -> item.name
    }

    val secondaryTitle = when (languageMode) {
        LanguageMode.DUAL -> item.nameKn
        else -> null
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hub_card_${item.id}")
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClick = { onOpenInBrowser(item) }
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = HubSurfaceDark
        ),
        border = BorderStroke(1.2.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Icon / Emoji badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(HubCyanAccent.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.iconEmoji.ifBlank { "🌐" },
                        fontSize = 20.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Favorite star/heart button
                    IconButton(
                        onClick = { onToggleFavorite(item) },
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("fav_btn_${item.id}")
                    ) {
                        Icon(
                            imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Toggle favorite",
                            tint = if (item.isFavorite) HubPinkAccent else HubTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Overflow Menu
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("menu_btn_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More actions",
                                tint = HubTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Open in App Browser") },
                                leadingIcon = {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, tint = HubCyanAccent)
                                },
                                onClick = {
                                    menuExpanded = false
                                    onOpenInBrowser(item)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Open in External Browser") },
                                leadingIcon = {
                                    Icon(Icons.Default.OpenInNew, contentDescription = null)
                                },
                                onClick = {
                                    menuExpanded = false
                                    openExternalBrowser(context, item.url)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Copy Link") },
                                leadingIcon = {
                                    Icon(Icons.Outlined.ContentCopy, contentDescription = null)
                                },
                                onClick = {
                                    menuExpanded = false
                                    copyToClipboard(context, item.name, item.url)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Share Link") },
                                leadingIcon = {
                                    Icon(Icons.Default.Share, contentDescription = null)
                                },
                                onClick = {
                                    menuExpanded = false
                                    shareLink(context, item.name, item.url)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Edit Details") },
                                leadingIcon = {
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                },
                                onClick = {
                                    menuExpanded = false
                                    onEdit(item)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Delete", color = Color(0xFFEF4444)) },
                                leadingIcon = {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444))
                                },
                                onClick = {
                                    menuExpanded = false
                                    onDelete(item)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Primary Title
            Text(
                text = primaryTitle,
                color = HubTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Secondary Title (Kannada subtitle if dual)
            if (!secondaryTitle.isNullOrBlank()) {
                Text(
                    text = secondaryTitle,
                    color = HubCyanAccent,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Host URL / Subtitle
            Text(
                text = displayHost,
                color = HubTextMuted,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun openExternalBrowser(context: Context, url: String) {
    try {
        val target = if (url.startsWith("http://") || url.startsWith("https://")) url else "https://$url"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(target)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
    }
}

private fun copyToClipboard(context: Context, name: String, url: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText(name, url)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
}

private fun shareLink(context: Context, name: String, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, name)
        putExtra(Intent.EXTRA_TEXT, "$name: $url")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(Intent.createChooser(intent, "Share $name"))
}
