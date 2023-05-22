package com.example.bookapp.navigation

enum class  BooksScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    HomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    StatsScreen;
    companion object{
        fun fromRoute(route: String?): BooksScreens
        = when (route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailScreen.name -> DetailScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")

        }
    }




    }
