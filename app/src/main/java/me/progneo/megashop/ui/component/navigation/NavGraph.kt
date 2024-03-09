package me.progneo.megashop.ui.component.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.progneo.megashop.ui.screen.main.MainScreen
import me.progneo.megashop.ui.screen.product.ProductScreen
import me.progneo.megashop.ui.screen.search.SearchScreen
import me.progneo.megashop.ui.util.NavDestinations

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavDestinations.MAIN_SCREEN
    ) {
        composable(route = NavDestinations.MAIN_SCREEN) {
            MainScreen(navController = navController)
        }
        composable(route = "${NavDestinations.PRODUCT_SCREEN}/{productId}") {
            ProductScreen(navController = navController)
        }
        composable(route = NavDestinations.SEARCH_SCREEN) {
            SearchScreen(navController = navController)
        }
    }
}
