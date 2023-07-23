package dgs.software.classicchess

import dgs.software.classicchess.di.classicChessModule
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.TestLogger
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.moves.MovePiece
import dgs.software.classicchess.model.toGame
import dgs.software.classicchess.model.toMutableGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class MutableGameTest {
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
    fun `test mutableGame deep copy`() {
        val originalGame = MutableGame()
        originalGame.executeMove(MovePiece(fromPos = Coordinate(1,1), toPos = Coordinate(3,1)))
        originalGame.executeMove(MovePiece(fromPos = Coordinate(6,1), toPos = Coordinate(5,1)))
        val copiedGame = originalGame.deepCopy()
        assertThat(copiedGame).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(originalGame)
    }

    @Test
    fun `modifying copy does not affect original game`() {
        val originalGame = MutableGame()
        originalGame.executeMove(MovePiece(fromPos = Coordinate(1,1), toPos = Coordinate(3,1)))
        originalGame.executeMove(MovePiece(fromPos = Coordinate(6,1), toPos = Coordinate(5,1)))
        val copiedGame = originalGame.deepCopy()
        assertThat(copiedGame).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(originalGame)
        originalGame.board.set(Coordinate(0, 0), null)
        assertThat(copiedGame).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isNotEqualTo(originalGame)
    }

    @Test
    fun `test mutableGame to game and back`() {
        val movePiece1 = MovePiece(fromPos = Coordinate(1,1), toPos = Coordinate(3,1))
        val movePiece2 = MovePiece(fromPos = Coordinate(6,1), toPos = Coordinate(5,1))
        val originalGame = MutableGame()
        originalGame.executeMove(movePiece1)
        originalGame.executeMove(movePiece2)
        val copiedGame = originalGame.deepCopy()

        val convertedGame = copiedGame.toGame().toMutableGame()

        assertThat(convertedGame).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(originalGame)
    }
}