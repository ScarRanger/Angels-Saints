package com.rhinepereira.saints.data.source

import com.rhinepereira.saints.data.model.Category
import com.rhinepereira.saints.data.model.Item

object FakeContentSource {
    val categories = listOf(
        Category("1", "Angels", "Learn about Angels", ""),
        Category("2", "Saints", "Learn about Saints", "")
    )

    val items = listOf(
        Item("1", "St. Michael", "Archangel", "", "1"),
        Item("2", "St. Francis", "Assisi", "", "2")
    )
}
