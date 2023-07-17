package dgs.software.classicchess.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dgs.software.classicchess.R
import dgs.software.classicchess.calculations.ai.Difficulty
import dgs.software.classicchess.ui.screens.local_game.LocalGameScreen
import dgs.software.classicchess.ui.screens.local_game.LocalGameViewModel
import dgs.software.classicchess.ui.screens.MainMenuScreen
import dgs.software.classicchess.ui.screens.computer_game.ComputerGameScreen
import dgs.software.classicchess.ui.screens.computer_game.ComputerGameViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

private const val TAG = "ClassicChessApp"

enum class ClassicChessScreen(@StringRes val title: Int, val route: String) {
    Menu(title = R.string.Empty, route = "menu"),
    LocalGame(title = R.string.local_game_screen, route = "local_game"),
    ComputerGame(title = R.string.computer_game_screen, route = "computer_game/{difficulty}") // Add {difficulty} here
}

@Composable
fun ClassicChessAppBar(
    currentScreen: ClassicChessScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun ClassicChessApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = getScreenFromRoute(
        backStackEntry?.destination?.route ?: ClassicChessScreen.Menu.route
    )

    val localGameViewModel: LocalGameViewModel = getViewModel()

    Scaffold(
        topBar = {
            ClassicChessAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ClassicChessScreen.Menu.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = ClassicChessScreen.Menu.name) {
                MainMenuScreen(
                    startLocalGame = {navController.navigate(ClassicChessScreen.LocalGame.name)},
                    startComputerGame = { difficulty ->
                        navController.navigate("${ClassicChessScreen.ComputerGame.route.replace("{difficulty}", difficulty.name)}")
                    }
                )
            }
            composable(route = ClassicChessScreen.LocalGame.name) {
                LocalGameScreen(localGameViewModel)
            }
            composable(route = ClassicChessScreen.ComputerGame.route) { backStackEntry ->
                val difficulty = backStackEntry.arguments?.getString("difficulty")?.let { Difficulty.valueOf(it) } ?: Difficulty.EASY
                val computerGameViewModel: ComputerGameViewModel = getViewModel { parametersOf(difficulty) }
                ComputerGameScreen(computerGameViewModel)
            }
        }
    }
}

private fun getScreenFromRoute(route: String): ClassicChessScreen {
    return when {
        route.startsWith(ClassicChessScreen.Menu.route) -> ClassicChessScreen.Menu
        route.startsWith(ClassicChessScreen.LocalGame.route) -> ClassicChessScreen.LocalGame
        route.startsWith(ClassicChessScreen.ComputerGame.route.substringBefore("{")) -> ClassicChessScreen.ComputerGame
        else -> {
            ClassicChessScreen.Menu
        }
    }
}