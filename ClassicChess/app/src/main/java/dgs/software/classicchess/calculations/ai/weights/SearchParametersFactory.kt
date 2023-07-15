package dgs.software.classicchess.calculations.ai.weights

import dgs.software.classicchess.calculations.ai.Difficulty

class SearchParametersFactory {
    companion object {
        fun create(difficulty: Difficulty): SearchParameters {
            return when (difficulty) {
                Difficulty.EASY -> SearchParametersEasy()
                Difficulty.NORMAL -> SearchParametersNormal()
                Difficulty.HARD -> SearchParametersHard()
            }
        }
    }
}