package dgs.software.classicchess

import android.annotation.SuppressLint
import dgs.software.classicchess.di.classicChessModule
import dgs.software.classicchess.logger.TestLogger
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.actions.*
import dgs.software.classicchess.model.moves.*
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class RevertableActionTest {
    @Before
    fun setup() {
        startKoin {
            modules(classicChessModule)
        }
        loadKoinModules(module {
            single() { TestLogger }
        })
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test move piece action deep copy`() {
        val original = MovePieceAction(Coordinate(1, 1), Coordinate(2, 2))
        val copy = original.deepCopy()

        Assertions.assertThat(copy).isNotSameAs(original)
        Assertions.assertThat(copy.fromPos).isEqualTo(original.fromPos)
        Assertions.assertThat(copy.toPos).isEqualTo(original.toPos)
        Assertions.assertThat(copy.isExecuted).isEqualTo(original.isExecuted)
    }

    @Test
    fun `test remove piece action deep copy`() {
        val original = RemovePieceAction(Coordinate(1, 1))
        original.execute(MutableGame())
        val copy = original.deepCopy()

        Assertions.assertThat(copy).isNotSameAs(original)
        Assertions.assertThat(copy.coordinate).isEqualTo(original.coordinate)
        Assertions.assertThat(copy.isExecuted).isEqualTo(original.isExecuted)
        Assertions.assertThat(copy.capturedPiece).isEqualTo(original.capturedPiece)
    }

    @Test
    fun `test ReplacePieceAction deepCopy`() {
        val original = ReplacePieceAction(Coordinate(1, 1), Type.QUEEN, true)
        val copy = original.deepCopy()

        Assertions.assertThat(copy).isNotSameAs(original)
        Assertions.assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun `test SetIsMovedAction deepCopy`() {
        val original = SetIsMovedAction(Coordinate(1, 1), true)
        val copy = original.deepCopy()

        Assertions.assertThat(copy).isNotSameAs(original)
        Assertions.assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun `test UpdateCurrentPlayerAction deepCopy`() {
        val original = UpdateCurrentPlayerAction(true)
        val copy = original.deepCopy()

        Assertions.assertThat(copy).isNotSameAs(original)
        Assertions.assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun testMovePieceAction() {
        val originalAction = MovePieceAction(Coordinate(1, 1), Coordinate(3, 1))
        val game = MutableGame()
        val originalGameState = game.copy()
        originalAction.execute(game)

        Assertions.assertThat(game.board.get(3, 1) != null)
        Assertions.assertThat(game.board.get(3, 1)?.equals(game.board.get(1, 1)))
        Assertions.assertThat(game.board.get(1, 1) == null)

        originalAction.rollback(game)

        val copiedAction = originalAction.deepCopy()

        Assertions.assertThat(copiedAction).usingRecursiveComparison().isEqualTo(originalAction)
        Assertions.assertThat(originalGameState).usingRecursiveComparison().isEqualTo(game)
    }

    @Test
    fun testRemovePieceAction() {
        val originalAction = RemovePieceAction(Coordinate(1, 1))
        val game = MutableGame()
        val originalGameState = game.copy()
        originalAction.execute(game)

        Assertions.assertThat(game.board.get(1, 1) == null)

        originalAction.rollback(game)

        val copiedAction = originalAction.deepCopy()

        Assertions.assertThat(copiedAction).usingRecursiveComparison().isEqualTo(originalAction)
        Assertions.assertThat(originalGameState).usingRecursiveComparison().isEqualTo(game)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testReplacePieceAction() {
        val originalAction = ReplacePieceAction(Coordinate(1, 1), Type.QUEEN)
        val game = MutableGame()
        val originalGameState = game.copy()
        originalAction.execute(game)

        Assertions.assertThat(game.board.get(1, 1)?.type == Type.QUEEN)
        Assertions.assertThat(
            game.board.get(1, 1)?.player == originalGameState.board.get(
                1,
                1
            )?.player
        )

        originalAction.rollback(game)

        val copiedAction = originalAction.deepCopy()

        Assertions.assertThat(copiedAction).usingRecursiveComparison().isEqualTo(originalAction)
        Assertions.assertThat(originalGameState).usingRecursiveComparison().isEqualTo(game)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testSetIsMovedAction() {
        val originalAction = SetIsMovedAction(Coordinate(1, 1))
        val game = MutableGame()
        val originalGameState = game.copy()
        originalAction.execute(game)

        Assertions.assertThat(game.board.get(1, 1)?.isMoved == true)

        originalAction.rollback(game)

        val copiedAction = originalAction.deepCopy()

        Assertions.assertThat(copiedAction).usingRecursiveComparison().isEqualTo(originalAction)
        Assertions.assertThat(originalGameState).usingRecursiveComparison().isEqualTo(game)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testUpdateCurrentPlayerAction() {
        val originalAction = UpdateCurrentPlayerAction()
        val game = MutableGame()
        val originalGameState = game.copy()
        originalAction.execute(game)

        Assertions.assertThat(game.currentPlayer == Player.BLACK)

        originalAction.rollback(game)

        val copiedAction = originalAction.deepCopy()

        Assertions.assertThat(copiedAction).usingRecursiveComparison().isEqualTo(originalAction)
        Assertions.assertThat(originalGameState).usingRecursiveComparison().isEqualTo(game)
    }
}
