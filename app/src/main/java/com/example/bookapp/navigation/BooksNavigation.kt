package com.example.bookapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.bookapp.screens.BooksAppSplashScreen
import com.example.bookapp.screens.details.BookDetailsScreen
import com.example.bookapp.screens.home.BookHomeScreen
import com.example.bookapp.screens.home.HomeScreenViewModel
import com.example.bookapp.screens.login.BookLoginScreen
import com.example.bookapp.screens.search.BooksSearchViewModel
import com.example.bookapp.screens.search.SearchScreen
import com.example.bookapp.screens.stats.BookStatsScreen
import com.example.bookapp.screens.update.BookUpdateScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BooksNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = BooksScreens.SplashScreen.name){
        composable(BooksScreens.SplashScreen.name){
            BooksAppSplashScreen(navController=navController )
        }
        composable(BooksScreens.HomeScreen.name){
            val homeViewModel= hiltViewModel<HomeScreenViewModel>()
            BookHomeScreen(navController=navController, viewModel=homeViewModel)
        }
        val updateName=BooksScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
        arguments = listOf(navArgument("bookItemId"){
            type=NavType.StringType
        })){navBackStackEntry->
            navBackStackEntry.arguments?.getString("bookItemId").let{
                BookUpdateScreen(navController = navController, bookItemId = it.toString())
            }

        }
        val detailName= BooksScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments =listOf(navArgument("bookId") {
            type= NavType.StringType
        })){backStackEntry -> backStackEntry.arguments?.getString("bookId").let{
            BookDetailsScreen(navController = navController,bookId=it.toString())
        }

        }
        composable(BooksScreens.LoginScreen.name){
            BookLoginScreen(navController = navController)
        }
        composable(BooksScreens.SearchScreen.name){
            val viewModel= hiltViewModel<BooksSearchViewModel>()
            SearchScreen(navController = navController,viewModel)
        }
        composable(BooksScreens.StatsScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            BookStatsScreen(navController = navController, viewModel=homeViewModel)
        }
    }
}