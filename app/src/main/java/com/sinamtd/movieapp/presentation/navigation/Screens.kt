package com.sinamtd.movieapp.presentation.navigation

sealed class Screens(val rout: String) {
    object Home: Screens("home")
    object Detail: Screens("detail")
    object Favorite: Screens("favorite")
    object Search: Screens("search")








    fun routeWithArgs(vararg args: String)=
        buildString {
            append(rout)
            args.forEachIndexed { index, _ ->
                append("?$index={$index}")
            }
        }

    fun paramsWithArgs(vararg args: String)=

        buildString {
            append(rout)
            args.forEachIndexed { index, value ->
                append("?$index=$value")
            }
        }


}
