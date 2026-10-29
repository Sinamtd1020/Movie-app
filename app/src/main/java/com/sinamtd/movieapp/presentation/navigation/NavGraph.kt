package com.sinamtd.movieapp.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sinamtd.movieapp.presentation.detail.DetailScreen
import com.sinamtd.movieapp.presentation.favorite.FavoriteScreen
import com.sinamtd.movieapp.presentation.home.HomeScreen
import com.sinamtd.movieapp.presentation.search.SearchScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetUpNavigation() {

    val navController = rememberNavController()

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        currentBackStackEntry?.destination?.route


    val showBottomBar =
        currentRoute == Screens.Home.rout ||
                currentRoute == Screens.Search.rout ||
                currentRoute == Screens.Favorite.rout


    Scaffold(

        bottomBar = {

            if (showBottomBar) {

                BottomBar(
                    navController = navController
                )
            }
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screens.Home.rout,
            modifier = Modifier.padding(
                paddingValues
            )
        ) {

            // HOME
            composable(
                route = Screens.Home.rout
            ) {

                HomeScreen(
                    navController = navController
                )
            }


            // SEARCH
            composable(
                route = Screens.Search.rout
            ) {

                SearchScreen(
                    navController = navController
                )
            }


            // FAVORITE
            composable(
                route = Screens.Favorite.rout
            ) {

                FavoriteScreen(
                    navController = navController
                )
            }



            // DETAIL
            composable(
                route = Screens.Detail.routeWithArgs("movieId")
            ) { backStackEntry ->

                val movieId =
                    backStackEntry
                        .arguments
                        ?.getString("0")
                        ?: return@composable

                DetailScreen(
                    movieId = movieId,
                    navController = navController
                )
            }
        }
    }
}