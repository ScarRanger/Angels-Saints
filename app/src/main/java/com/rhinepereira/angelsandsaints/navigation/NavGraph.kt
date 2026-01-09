package com.rhinepereira.angelsandsaints.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rhinepereira.angelsandsaints.category.CategoryScreen
import com.rhinepereira.angelsandsaints.category.CategoryViewModel
import com.rhinepereira.angelsandsaints.content.ContentScreen
import com.rhinepereira.angelsandsaints.content.ContentViewModel
import com.rhinepereira.angelsandsaints.data.repository.ContentRepository
import com.rhinepereira.angelsandsaints.home.HomeScreen
import com.rhinepereira.angelsandsaints.home.HomeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: ContentRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return HomeViewModel(repository) as T
                    }
                }
            )
            HomeScreen(
                viewModel = viewModel,
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.Category.createRoute(categoryId))
                }
            )
        }

        composable(
            route = Screen.Category.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
            val viewModel: CategoryViewModel = viewModel(
                key = categoryId,
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CategoryViewModel(repository, categoryId) as T
                    }
                }
            )
            CategoryScreen(
                viewModel = viewModel,
                onItemClick = { contentId ->
                    navController.navigate(Screen.Content.createRoute(categoryId, contentId))
                }
            )
        }

        composable(
            route = Screen.Content.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("contentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
            val contentId = backStackEntry.arguments?.getString("contentId") ?: return@composable
            val viewModel: ContentViewModel = viewModel(
                key = contentId,
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ContentViewModel(repository, categoryId, contentId) as T
                    }
                }
            )
            ContentScreen(viewModel = viewModel)
        }
    }
}
