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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dgs.software.classicchess.R
import dgs.software.classicchess.ui.screens.LocalGameScreen
import dgs.software.classicchess.ui.screens.LocalGameViewModel
import dgs.software.classicchess.ui.screens.MainMenuScreen

enum class ClassicChessScreen(@StringRes val title: Int) {
    Menu(title = R.string.Empty),
    LocalGame(title = R.string.local_game_screen)
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
    val currentScreen = ClassicChessScreen.valueOf(
        backStackEntry?.destination?.route ?: ClassicChessScreen.Menu.name
    )

    val localGameViewModel: LocalGameViewModel =
        viewModel(factory = LocalGameViewModel.Factory)

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
                    onLocalGameButtonClickedAction = {navController.navigate(ClassicChessScreen.LocalGame.name)},
                    onComputerGameButtonClickedAction = {}
                )
            }
            composable(route = ClassicChessScreen.LocalGame.name) {
                LocalGameScreen(localGameViewModel)
            }
        }
    }

}