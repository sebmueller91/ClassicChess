package dgs.software.classicchess.calculations.ai

import dgs.software.classicchess.calculations.ai.weights.SearchParametersFactory
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.moves.RevertableMove

class AiMoveCalculator(
    difficulty: Difficulty,
    private val aiPlayer: Player
) {
    private val searchParameters = SearchParametersFactory.create(difficulty)
    private val evaluationFunction = EvaluationFunction(searchParameters)
    private val minMaxSearch = MinMaxSearch(evaluationFunction)

    fun calculateAiMove(mutableGame: MutableGame): RevertableMove {
        return minMaxSearch.search(mutableGame, aiPlayer)
    }
}