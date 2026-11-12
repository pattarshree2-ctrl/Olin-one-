package com.example.data.local

import com.example.data.model.Categories
import com.example.data.model.HubItem

object DefaultHubData {
    val INITIAL_ITEMS = listOf(
        // Social Media
        HubItem(
            name = "Facebook",
            nameKn = "ಫೇಸ್‌ಬುಕ್",
            url = "https://www.facebook.com",
            categoryId = Categories.SOCIAL.id,
            iconEmoji = "📘",
            description = "Social networking & community",
            orderIndex = 1
        ),
        HubItem(
            name = "Instagram",
            nameKn = "ಇನ್‌ಸ್ಟಾಗ್ರಾಮ್",
            url = "https://www.instagram.com",
            categoryId = Categories.SOCIAL.id,
            iconEmoji = "📸",
            description = "Photo & Reels sharing",
            orderIndex = 2
        ),
        HubItem(
            name = "WhatsApp Web",
            nameKn = "ವಾಟ್ಸಾಪ್ ವೆಬ್",
            url = "https://web.whatsapp.com",
            categoryId = Categories.SOCIAL.id,
            iconEmoji = "💬",
            description = "Instant messaging & chats",
            orderIndex = 3
        ),

        // AI Assistant
        HubItem(
            name = "Gemini AI",
            nameKn = "ಜೆಮಿನಿ AI",
            url = "https://gemini.google.com",
            categoryId = Categories.AI_ASSISTANT.id,
            iconEmoji = "✨",
            isFavorite = true,
            description = "Google's smart multimodal AI",
            orderIndex = 4
        ),
        HubItem(
            name = "Meta AI",
            nameKn = "ಮೆಟಾ AI",
            url = "https://www.meta.ai",
            categoryId = Categories.AI_ASSISTANT.id,
            iconEmoji = "🧠",
            description = "Llama-powered intelligent assistant",
            orderIndex = 5
        ),
        HubItem(
            name = "Flow AI",
            nameKn = "ಫ್ಲೋ AI",
            url = "https://flowgpt.com",
            categoryId = Categories.AI_ASSISTANT.id,
            iconEmoji = "⚡",
            description = "Prompt community & AI agents",
            orderIndex = 6
        ),

        // AI Image Generator
        HubItem(
            name = "Bing Image Creator",
            nameKn = "ಬಿಂಗ್ ಇಮೇಜ್ ಕ್ರಿಯೇಟರ್",
            url = "https://www.bing.com/create",
            categoryId = Categories.AI_IMAGE.id,
            iconEmoji = "🎨",
            isFavorite = true,
            description = "DALL-E 3 image generation",
            orderIndex = 7
        ),
        HubItem(
            name = "Midjourney",
            nameKn = "ಮಿಡ್‌ಜರ್ನಿ",
            url = "https://www.midjourney.com",
            categoryId = Categories.AI_IMAGE.id,
            iconEmoji = "🖼️",
            description = "Photorealistic AI artwork generation",
            orderIndex = 8
        ),
        HubItem(
            name = "Stable Diffusion (Local)",
            nameKn = "ಸ್ಟೇಬಲ್ ಡಿಫ್ಯೂಷನ್",
            url = "http://127.0.0.1:7860",
            categoryId = Categories.AI_IMAGE.id,
            iconEmoji = "💻",
            description = "Local WebUI image generation",
            orderIndex = 9
        ),

        // AI Video Generator
        HubItem(
            name = "Hailuo AI Video",
            nameKn = "ಹೈಲುವೋ AI ವಿಡಿಯೋ",
            url = "https://hailuoai.video",
            categoryId = Categories.AI_VIDEO.id,
            iconEmoji = "🎬",
            isFavorite = true,
            description = "High quality text-to-video AI",
            orderIndex = 10
        ),
        HubItem(
            name = "Runway AI Video",
            nameKn = "ರನ್‌ವೇ AI ವಿಡಿಯೋ",
            url = "https://app.runwayml.com",
            categoryId = Categories.AI_VIDEO.id,
            iconEmoji = "🎥",
            description = "Gen-3 Alpha generative video studio",
            orderIndex = 11
        ),

        // AI Image-to-Video
        HubItem(
            name = "Runway Gen-2",
            nameKn = "ರನ್‌ವೇ Gen-2",
            url = "https://app.runwayml.com",
            categoryId = Categories.AI_IMG_VIDEO.id,
            iconEmoji = "🔄",
            description = "Animate still images into motion",
            orderIndex = 12
        ),
        HubItem(
            name = "Luma Dream Machine",
            nameKn = "ಲ್ಯುಮಾ ಡ್ರೀಮ್ ಮಷೀನ್",
            url = "https://luma.ai/dream-machine",
            categoryId = Categories.AI_IMG_VIDEO.id,
            iconEmoji = "🌌",
            description = "Hyper-realistic camera movement video",
            orderIndex = 13
        ),

        // AI Song Creator
        HubItem(
            name = "Suno AI Songs",
            nameKn = "ಸುನೋ AI ಹಾಡುಗಳು",
            url = "https://suno.com",
            categoryId = Categories.AI_SONG.id,
            iconEmoji = "🎵",
            isFavorite = true,
            description = "Generate full songs with lyrics & vocals",
            orderIndex = 14
        ),
        HubItem(
            name = "Udio Music",
            nameKn = "ಉಡಿಯೋ ಮ್ಯೂಸಿಕ್",
            url = "https://www.udio.com",
            categoryId = Categories.AI_SONG.id,
            iconEmoji = "🎧",
            description = "Expressive vocal & track creation",
            orderIndex = 15
        ),

        // File & App Sharing
        HubItem(
            name = "ShareDrop (Web Send)",
            nameKn = "ಶೇರ್‌ಡ್ರಾಪ್ ವೆಬ್ ಸೆಂಡ್",
            url = "https://www.sharedrop.io",
            categoryId = Categories.FILE_SHARING.id,
            iconEmoji = "🌐",
            description = "Peer-to-peer browser file transfer",
            orderIndex = 16
        ),
        HubItem(
            name = "SnapDrop File Send",
            nameKn = "ಸ್ನ್ಯಾಪ್‌ಡ್ರಾಪ್ ಫೈಲ್ ಸೆಂಡ್",
            url = "https://snapdrop.net",
            categoryId = Categories.FILE_SHARING.id,
            iconEmoji = "📡",
            description = "Local WiFi instant file sender",
            orderIndex = 17
        )
    )
}
