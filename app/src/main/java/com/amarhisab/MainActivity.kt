
package com.amarhisab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amarhisab.ui.screens.*
import com.amarhisab.ui.viewmodel.MainViewModel
import com.amarhisab.ui.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as AmarHisabApp
        val viewModel = MainViewModel(app.database.dao())

        setContent {
            AmarHisabTheme {
                MainContainer(viewModel)
            }
        }
    }
}

@Composable
fun MainContainer(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Dues,
        Screen.Savings,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(id = screen.icon), contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { HomeScreen(viewModel) }
            composable(Screen.Dues.route) { DuesScreen(viewModel) }
            composable(Screen.Savings.route) { SavingsScreen(viewModel) }
            composable(Screen.Profile.route) { ProfileScreen(viewModel) }
        }
    }
}

sealed class Screen(val route: String, val resourceId: Int, val icon: Int) {
    object Home : Screen("home", R.string.home, android.R.drawable.ic_menu_today)
    object Dues : Screen("dues", R.string.dues, android.R.drawable.ic_menu_recent_history)
    object Savings : Screen("savings", R.string.savings, android.R.drawable.ic_menu_save)
    object Profile : Screen("profile", R.string.profile, android.R.drawable.ic_menu_myplaces)
}

@Composable
fun AmarHisabTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2E7D32),
            secondary = Color(0xFF4CAF50),
            tertiary = Color(0xFFFB8C00)
        ),
        content = content
    )
}
