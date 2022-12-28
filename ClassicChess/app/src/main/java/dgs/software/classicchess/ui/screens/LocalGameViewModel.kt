package dgs.software.classicchess.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dgs.software.classicchess.ClassicChessApplication
import dgs.software.classicchess.model.Game

class LocalGameViewModel : ViewModel() {
    var gameUiState: Game by mutableStateOf(Game())
        private set

    init {
        gameUiState = Game()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClassicChessApplication)
                LocalGameViewModel()
            }
        }
    }
}