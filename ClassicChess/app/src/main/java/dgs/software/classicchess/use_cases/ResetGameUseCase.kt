package dgs.software.classicchess.use_cases

import dgs.software.classicchess.ui.screens.LocalGameUiState

class ResetGameUseCase {
    fun execute(boardDisplayedInverted: Boolean): LocalGameUiState {
        return LocalGameUiState(boardDisplayedInverted = boardDisplayedInverted)
    }
}