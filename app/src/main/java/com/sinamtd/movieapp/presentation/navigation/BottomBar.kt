package com.sinamtd.movieapp.presentation.navigation



import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sinamtd.movieapp.R


@Composable
fun BottomBar(
    navController: NavController
) {

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        currentBackStackEntry
            ?.destination
            ?.route


    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == Screens.Home.rout,
            onClick = {

                navController.navigate(
                    Screens.Home.rout
                ) {

                    popUpTo(
                        navController.graph.startDestinationId
                    ) {
                        saveState=true
                    }

                    launchSingleTop = true
                    restoreState=true
                }
            },
            icon = {

                Icon(
                    imageVector = if (currentRoute== Screens.Home.rout)Icons.Default.Home
                    else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text("Home")
            },
            colors = NavigationBarItemColors(selectedTextColor = Color.Black
                , unselectedTextColor = Color.Gray
                , selectedIconColor = Color.Black
                , unselectedIconColor = Color.Gray, disabledIconColor = Color.Black, disabledTextColor = Color.Black ,selectedIndicatorColor = Color.Transparent)
        )


        NavigationBarItem(
            selected = currentRoute == Screens.Search.rout,
            onClick = {

                navController.navigate(
                    Screens.Search.rout
                ) {
                    popUpTo(navController.graph.startDestinationId){
                        saveState=true
                    }

                    launchSingleTop = true
                    restoreState=true
                }
            },
            icon = {

               Icon(
                    painter = if (currentRoute== Screens.Search.rout) painterResource(R.drawable.defultsearch)
                    else painterResource(R.drawable.search)
                    ,
                    contentDescription = "Search",
                   modifier = Modifier.size(20.dp)
                )
            },
            label = {
                Text("Search")
            },
            colors = NavigationBarItemColors(selectedTextColor = Color.Black
                , unselectedTextColor = Color.Gray
                , selectedIconColor = Color.Black
                , unselectedIconColor = Color.Gray, disabledIconColor = Color.Black, disabledTextColor = Color.Black ,selectedIndicatorColor = Color.Transparent)

        )


        NavigationBarItem(
            selected = currentRoute == Screens.Favorite.rout,
            onClick = {

                navController.navigate(
                    Screens.Favorite.rout
                ) {
                    popUpTo(navController.graph.startDestinationId){
                        saveState=true
                    }

                    launchSingleTop = true
                    restoreState=true
                }
            },
            icon = {

                Icon(
                    imageVector =if (currentRoute== Screens.Favorite.rout) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            },
            label = {
                Text("Favorite")
            },
            colors = NavigationBarItemColors(selectedTextColor = Color.Black
                , unselectedTextColor = Color.Gray
                , selectedIconColor = Color.Black
                , unselectedIconColor = Color.Gray, disabledIconColor = Color.Black, disabledTextColor = Color.Black ,selectedIndicatorColor = Color.Transparent)

        )
    }
}