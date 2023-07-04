package dgs.software.classicchess.di

import dgs.software.classicchess.ui.screens.LocalGameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val classicChessModule = module {
    viewModel {
        LocalGameViewModel()
    }
}