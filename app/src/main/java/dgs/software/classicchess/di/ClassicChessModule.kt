package dgs.software.classicchess.di

import dgs.software.classicchess.calculations.ai.Difficulty
import dgs.software.classicchess.logger.AndroidLogger
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.LoggerFactory
import dgs.software.classicchess.ui.screens.computer_game.ComputerGameViewModel
import dgs.software.classicchess.ui.screens.local_game.LocalGameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val classicChessModule = module {
    single<Logger> { AndroidLogger }

    viewModel {
        LocalGameViewModel(logger = get())
    }

    viewModel { (difficulty: Difficulty) -> ComputerGameViewModel(difficulty = difficulty, logger = get()) }
}