package dgs.software.classicchess.di

import dgs.software.classicchess.calculations.possiblemoves.*
import dgs.software.classicchess.ui.screens.LocalGameViewModel
import dgs.software.classicchess.use_cases.GetPossibleMovesUseCase
import dgs.software.classicchess.use_cases.UpdateGameStatusUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val classicChessModule = module {
    single { BasicMovesProvider() }
    single { BoardStatusProvider(basicMovesProvider = get()) }
    single {
        PossibleMovesProvider(
            basicPossibleMovesProvider = get(),
            boardStatusProvider = get()
        )
    }
    single { GameStatusProvider(possibleMovesProvider = get(), boardStatusProvider = get()) }

    single { UpdateGameStatusUseCase(gameStatusProvider = get(), boardStatusProvider = get())}
    single {GetPossibleMovesUseCase(possibleMovesProvider = get())}

    viewModel {
        LocalGameViewModel(getPossibleMovesUseCase = get(), updateGameStatusUseCase = get())
    }
}