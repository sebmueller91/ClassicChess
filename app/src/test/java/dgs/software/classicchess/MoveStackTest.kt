package dgs.software.classicchess

import dgs.software.classicchess.di.classicChessModule
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.TestLogger
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.moves.MoveAndCapturePiece
import dgs.software.classicchess.model.moves.MovePiece
import dgs.software.classicchess.model.moves.move_stack.MoveStack
import dgs.software.classicchess.model.toGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class MoveStackTest {
    @Before
    fun setup() {
        startKoin {
            modules(classicChessModule)
        }
        loadKoinModules(module {
            single() { TestLogger as Logger }
        })
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test MoveStack deepCopy`() {
        val mutableGame = MutableGame()
        val original = MoveStack().apply {
            executeMove(mutableGame, MovePiece(Coordinate(1, 1), Coordinate(2, 2), true))
            executeMove(mutableGame, MoveAndCapturePiece(Coordinate(3, 3), Coordinate(4, 4), true))
        }
        val copy = original.deepCopy()

        assertThat(copy).isNotSameAs(original)
        assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun rollbackRestoresGame() {
        val game = MutableGame()
        val originalGameState = game.deepCopy()

        val moveStack = MoveStack()

        val moves = listOf(
            MovePiece(Coordinate(1, 1), Coordinate(3, 1)),
            MovePiece(Coordinate(6, 6), Coordinate(5, 6)),
            MovePiece(Coordinate(3, 1), Coordinate(4, 1))
        )

        moves.forEach {move ->
            moveStack.executeMove(game, move)
        }

        moves.forEach {move ->
            assertThat(move.isExecuted)
        }

        assertThat(game.board).usingRecursiveComparison().isNotEqualTo(originalGameState.board)
        val modifiedGame = game.deepCopy()

        moves.forEach {
            moveStack.rollbackLastMove(game)
        }

        assertThat(game.simulatableMoveStack.moveStack.moves.size == 3)
        assertThat(game.board).usingRecursiveComparison().isEqualTo(originalGameState.board)

        moves.forEach {
            moveStack.redoNextMove(game)
        }

        assertThat(game.simulatableMoveStack.moveStack.moves.size == 3)
        assertThat(game.board).usingRecursiveComparison().isEqualTo(modifiedGame.board)
        assertThat(game.board).usingRecursiveComparison().isNotEqualTo(originalGameState.board)
    }

    @Test
    fun rollbackAndDeleteLastMoveRestoresGame() {
        val game = MutableGame()
        val originalGameState = game.deepCopy()

        val moveStack = MoveStack()

        val moves = listOf(
            MovePiece(Coordinate(1, 1), Coordinate(3, 1)),
            MovePiece(Coordinate(6, 6), Coordinate(5, 6)),
            MovePiece(Coordinate(3, 1), Coordinate(4, 1))
        )

        moves.forEach {move ->
            moveStack.executeMove(game, move)
        }

        moves.forEach {move ->
            assertThat(move.isExecuted)
        }

        moves.forEach { move ->
            moveStack.rollbackAndDeleteLastMove(game)
        }

        assertThat(game).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(originalGameState)
    }
}