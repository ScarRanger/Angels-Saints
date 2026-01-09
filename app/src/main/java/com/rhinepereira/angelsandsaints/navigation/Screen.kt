package com.rhinepereira.angelsandsaints.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Category : Screen("category/{categoryId}") {
        fun createRoute(categoryId: String) = "category/$categoryId"
    }
    object Content : Screen("content/{contentId}") {
        fun createRoute(contentId: String) = "content/$contentId"
    }
}
