package dgs.software.classicchess

import android.annotation.SuppressLint
import dgs.software.classicchess.di.classicChessModule
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.TestLogger
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class RevertableMoveTest {
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
    fun `test MovePiece deepCopy`() {
        val original = MovePiece(Coordinate(1, 1), Coordinate(2, 2), true)
        val copy = original.deepCopy()

        assertThat(copy).isNotSameAs(original)
        assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun `test PromotePawnMove deepCopy`() {
        val original = PromotePawnMove(Coordinate(1, 1), Coordinate(2, 2), Type.QUEEN, true, true)
        val copy = original.deepCopy()

        assertThat(copy).isNotSameAs(original)
        assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun `test MoveAndCapturePiece deepCopy`() {
        val original = MoveAndCapturePiece(Coordinate(1, 1), Coordinate(2, 2), true)
        val copy = original.deepCopy()

        assertThat(copy).isNotSameAs(original)
        assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun `test CastlingMove deepCopy`() {
        val original = CastlingMove(
            Coordinate(1, 1),
            Coordinate(2, 2),
            Coordinate(3, 3),
            Coordinate(4, 4),
            true
        )
        val copy = original.deepCopy()

        assertThat(copy).isNotSameAs(original)
        assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @Test
    fun `test CaptureEnPassantMove deepCopy`() {
        val original =
            CaptureEnPassantMove(Coordinate(1, 1), Coordinate(2, 2), true, Coordinate(3, 3))
        val copy = original.deepCopy()

        assertThat(copy).isNotSameAs(original)
        assertThat(copy).usingRecursiveComparison().isEqualTo(original)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testMovePiece() {
        val originalMove = MovePiece(Coordinate(1, 1), Coordinate(3, 1))
        val game = MutableGame()
        val originalGameState = game.copy()
        originalMove.execute(game)

        assertThat(game.board.get(3, 1) != null)
        assertThat(game.board.get(3, 1)?.equals(game.board.get(1, 1)))
        assertThat(game.board.get(1, 1) == null)
        assertThat(game.currentPlayer != originalGameState.currentPlayer)
        assertThat(game.board.get(3, 1)?.isMoved == false)

        originalMove.rollback(game)

        val copiedMove = originalMove.deepCopy()

        assertThat(copiedMove).usingRecursiveComparison().isEqualTo(originalMove)
        assertThat(originalGameState).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(game)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testPromotePawnMove() {
        val originalMove =
            PromotePawnMove(Coordinate(6, 7), Coordinate(7, 7), Type.QUEEN, captureAndMove = false)
        val game = MutableGame()
        game.board.set(Coordinate(7, 7), null)
        game.board.set(Coordinate(6, 7), Piece(Type.PAWN, Player.BLACK, true))
        val originalGameState = game.copy()
        originalMove.execute(game)

        assertThat(game.board.get(7, 7) != null)
        assertThat(game.board.get(7, 7)?.type == Type.QUEEN)
        assertThat(game.board.get(6, 7) == null)
        assertThat(game.currentPlayer != originalGameState.currentPlayer)

        originalMove.rollback(game)

        val copiedMove = originalMove.deepCopy()

        assertThat(copiedMove).usingRecursiveComparison().isEqualTo(originalMove)
        assertThat(originalGameState).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(game)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testPromotePawnMoveWithCapture() {
        val originalMove =
            PromotePawnMove(Coordinate(6, 7), Coordinate(7, 6), Type.QUEEN, captureAndMove = true)
        val game = MutableGame()
        game.board.set(Coordinate(7, 7), null)
        game.board.set(Coordinate(6, 7), Piece(Type.PAWN, Player.BLACK, true))
        val originalGameState = game.copy()
        originalMove.execute(game)

        assertThat(game.board.get(6, 7) != null)
        assertThat(game.board.get(7, 6)?.type == Type.QUEEN)
        assertThat(game.board.get(7, 6) == null)
        assertThat(game.currentPlayer != originalGameState.currentPlayer)

        originalMove.rollback(game)

        val copiedMove = originalMove.deepCopy()

        assertThat(copiedMove).usingRecursiveComparison().isEqualTo(originalMove)
        assertThat(originalGameState).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(game)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testMoveAndCapturePiece() {
        val originalMove = MoveAndCapturePiece(Coordinate(5, 6), Coordinate(6, 5))
        val game = MutableGame()
        game.board.set(Coordinate(5, 6), Piece(Type.PAWN, Player.BLACK, true))
        val originalGameState = game.copy()
        originalMove.execute(game)

        assertThat(game.board.get(6, 5) != null)
        assertThat(game.board.get(5, 6) == null)
        assertThat(game.board.get(6, 5)?.equals(originalGameState.board.get(5, 6)))
        assertThat(game.currentPlayer != originalGameState.currentPlayer)

        originalMove.rollback(game)

        val copiedMove = originalMove.deepCopy()

        assertThat(copiedMove).usingRecursiveComparison().isEqualTo(originalMove)
        assertThat(originalGameState).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(game)
    }

    @Test
    fun testCastlingMove() {
        val originalMove =
            CastlingMove(Coordinate(7, 4), Coordinate(7, 6), Coordinate(7, 7), Coordinate(7, 5))
        val game = MutableGame()
        game.board.set(Coordinate(7, 5), null)
        game.board.set(Coordinate(7, 6), null)
        val originalGameState = game.copy()
        originalMove.execute(game)

        assertThat(game.board.get(7, 7) == null)
        assertThat(game.board.get(7, 4) == null)
        assertThat(game.board.get(7, 6)?.equals(originalGameState.board.get(7, 4)))
        assertThat(game.board.get(7, 5)?.equals(originalGameState.board.get(7, 7)))
        assertThat(game.currentPlayer != originalGameState.currentPlayer)

        originalMove.rollback(game)

        val copiedMove = originalMove.deepCopy()

        assertThat(copiedMove).usingRecursiveComparison().isEqualTo(originalMove)
        assertThat(originalGameState).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(game)
    }

    @SuppressLint("CheckResult")
    @Test
    fun testCaptureEnPassantMove() {
        val originalMove = CaptureEnPassantMove(
            fromPos = Coordinate(4, 1),
            toPos = Coordinate(3, 0),
            capturePiecePos = Coordinate(3, 0)
        )
        val game = MutableGame()
        game.board.set(Coordinate(4, 1), Piece(Type.PAWN, Player.WHITE))
        game.executeMove(MovePiece(Coordinate(1, 0), Coordinate(3, 0)))
        val originalGameState = game.copy()

        originalMove.execute(game)

        assertThat(game.board.get(3, 0)?.equals(game.board.get(4, 1)))
        assertThat(game.board.get(Coordinate(3, 0)) == null)
        assertThat(game.board.get(4, 1) == null)
        assertThat(game.currentPlayer != originalGameState.currentPlayer)

        originalMove.rollback(game)

        val copiedMove = originalMove.deepCopy()

        assertThat(copiedMove).usingRecursiveComparison().isEqualTo(originalMove)
        assertThat(originalGameState).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(game)
    }
}