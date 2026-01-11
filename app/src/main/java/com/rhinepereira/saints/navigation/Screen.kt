package com.rhinepereira.saints.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Category : Screen("category/{categoryId}") {
        fun createRoute(categoryId: String) = "category/$categoryId"
    }
    object Content : Screen("content/{categoryId}/{contentId}") {
        fun createRoute(categoryId: String, contentId: String) = "content/$categoryId/$contentId"
    }
}
