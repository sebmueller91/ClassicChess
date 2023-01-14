package dgs.software.classicchess.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dgs.software.classicchess.ClassicChessApplication
import dgs.software.classicchess.calculations.possiblemoves.DefaultPossibleMovesProvider
import dgs.software.classicchess.calculations.possiblemoves.PossibleMovesProvider
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.RevertableMove

class LocalGameViewModel : ViewModel() {
    var gameUiState: Game by mutableStateOf(Game())
        private set

    var selectedCell: Cell? by mutableStateOf(null)
        private set

    var possibleMovesForSelectedPiece by mutableStateOf(mutableListOf<RevertableMove>())

    private val possibleMovesProvider = DefaultPossibleMovesProvider(gameUiState)

    // TODO: Think about better name
    fun cellSelected(coordinate: Coordinate) {
        val newGame = gameUiState.copy()

        selectedCell = newGame.get(coordinate)

        val clickedMove = possibleMovesForSelectedPiece.filter { it.toPos == coordinate }
        if (clickedMove.any()) {
            newGame.executeMove(clickedMove.first())
            val a = gameUiState.getBoard().get(Coordinate(5,0))
            val b = gameUiState.getBoard().get(Coordinate(6,0))

            gameUiState = newGame
        }
        possibleMovesForSelectedPiece.clear()
        if (selectedCell is Cell.Empty) {
            return
        }

        val selectedPiece = selectedCell as Cell.Piece
        if (selectedPiece.player == newGame.getCurrentPlayer()) {
            possibleMovesForSelectedPiece.addAll(
                possibleMovesProvider.getPossibleMoves(
                    selectedPiece.coordinate
                )
            )
        }


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