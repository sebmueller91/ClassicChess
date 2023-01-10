package dgs.software.classicchess.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dgs.software.classicchess.ClassicChessApplication
import dgs.software.classicchess.model.*

class LocalGameViewModel : ViewModel() {
    var gameUiState: Game by mutableStateOf(Game())
        private set

    var selectedCell: Coordinate? by mutableStateOf(null)
        private set

    // TODO: Think about better name
    fun assignSelectedCell(coordinate: Coordinate) {
        selectedCell = coordinate
    }

    // returns true if it is a cell with light background, false otherwise
    fun getCellBackgroundType(rowIndex: Int, colIndex: Int): Boolean {
        return (rowIndex % 2 == 0 && colIndex % 2 == 0) || (rowIndex % 2 != 0 && colIndex % 2 != 0)
    }

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