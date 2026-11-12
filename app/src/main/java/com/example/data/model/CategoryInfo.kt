package com.example.data.model

data class CategoryInfo(
    val id: String,
    val titleEn: String,
    val titleKn: String,
    val emoji: String,
    val order: Int
) {
    fun localizedTitle(mode: LanguageMode): String = when (mode) {
        LanguageMode.KANNADA -> "$emoji $titleKn"
        LanguageMode.ENGLISH -> "$emoji $titleEn"
        LanguageMode.DUAL -> "$emoji $titleKn ($titleEn)"
    }
}

object Categories {
    val ALL = CategoryInfo("all", "All Links", "ಎಲ್ಲಾ ಲಿಂಕ್‌ಗಳು", "🌟", 0)
    val FAVORITES = CategoryInfo("favorites", "Favorites", "ನೆಚ್ಚಿನವುಗಳು", "⭐", 1)

    val SOCIAL = CategoryInfo("social", "Social Media", "ಸೋಶಿಯಲ್ ಮೀಡಿಯಾ ವಿಭಾಗ", "🌐", 2)
    val AI_ASSISTANT = CategoryInfo("ai_assistant", "AI Assistant", "AI ಅಸಿಸ್ಟೆಂಟ್ ವಿಭಾಗ", "🤖", 3)
    val AI_IMAGE = CategoryInfo("ai_image", "AI Image Generator", "AI ಇಮೇಜ್ ಜನರೇಟರ್ ವಿಭಾಗ", "🎨", 4)
    val AI_VIDEO = CategoryInfo("ai_video", "AI Video Generator", "AI ವಿಡಿಯೋ ಜನರೇಟರ್ ವಿಭಾಗ", "🎬", 5)
    val AI_IMG_VIDEO = CategoryInfo("ai_img_video", "AI Image to Video", "AI ಇಮೇಜ್-ಟು-ವಿಡಿಯೋ", "🔄", 6)
    val AI_SONG = CategoryInfo("ai_music", "AI Song Creator", "AI ಸಾಂಗ್ ಕ್ರಿಯೇಟರ್ ವಿಭಾಗ", "🎵", 7)
    val FILE_SHARING = CategoryInfo("file_sharing", "File & App Sharing", "ಫೈಲ್ & ಆಪ್ ಶೇರಿಂಗ್ ವಿಭಾಗ", "📂", 8)
    val CUSTOM = CategoryInfo("custom", "Custom Added", "ಹೊಸದಾಗಿ ಸೇರಿಸಿದವು", "✨", 9)

    val SECTIONS = listOf(
        SOCIAL,
        AI_ASSISTANT,
        AI_IMAGE,
        AI_VIDEO,
        AI_IMG_VIDEO,
        AI_SONG,
        FILE_SHARING,
        CUSTOM
    )

    val FILTER_TABS = listOf(
        ALL,
        FAVORITES,
        SOCIAL,
        AI_ASSISTANT,
        AI_IMAGE,
        AI_VIDEO,
        AI_IMG_VIDEO,
        AI_SONG,
        FILE_SHARING,
        CUSTOM
    )

    fun getCategory(id: String): CategoryInfo {
        return SECTIONS.find { it.id == id } ?: CategoryInfo(id, id.replaceFirstChar { it.uppercase() }, id, "🔗", 99)
    }
}
