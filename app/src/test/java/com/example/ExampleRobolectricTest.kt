package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AI & Social Hub", appName)
  }

  @Test
  fun `verify initial hub data loaded`() {
    val items = com.example.data.local.DefaultHubData.INITIAL_ITEMS
    assert(items.isNotEmpty())
    assert(items.any { it.name == "Gemini AI" })
    assert(items.any { it.name == "Facebook" })
  }

  @Test
  fun `verify search filters by website title`() {
    val items = com.example.data.local.DefaultHubData.INITIAL_ITEMS
    val query = "gemini"
    val filtered = items.filter { it.name.lowercase().contains(query) || (it.nameKn?.lowercase()?.contains(query) == true) }
    assertEquals(1, filtered.size)
    assertEquals("Gemini AI", filtered.first().name)
  }

  @Test
  fun `verify search filters by category title`() {
    val sections = com.example.data.model.Categories.SECTIONS
    val query = "video"
    val matchedCategories = sections.filter { it.titleEn.lowercase().contains(query) || it.titleKn.lowercase().contains(query) }
    assert(matchedCategories.any { it.id == "ai_video" })
    assert(matchedCategories.any { it.id == "ai_img_video" })
  }
}
