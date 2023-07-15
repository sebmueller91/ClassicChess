package dgs.software.classicchess.di

import dgs.software.classicchess.calculations.ai.Difficulty
import dgs.software.classicchess.ui.screens.computer_game.ComputerGameViewModel
import dgs.software.classicchess.ui.screens.local_game.LocalGameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val classicChessModule = module {
    viewModel {
        LocalGameViewModel()
    }

    viewModel { (difficulty: Difficulty) -> ComputerGameViewModel(difficulty = difficulty) }
}