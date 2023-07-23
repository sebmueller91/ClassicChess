package dgs.software.classicchess

import dgs.software.classicchess.di.classicChessModule
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.TestLogger
import dgs.software.classicchess.model.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class BoardTest {
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
    fun `test Board deepCopy`() {
        val original = Board().apply {
            set(Coordinate(1, 1), Piece(Type.PAWN, Player.WHITE))
            set(Coordinate(2, 2), Piece(Type.QUEEN, Player.BLACK, isMoved = true))
        }
        val copy = original.deepCopy()

        assertThat(copy).isNotSameAs(original)
        assertThat(copy).usingRecursiveComparison().ignoringFields("zobristTable", "currentPlayerZobristNumber").isEqualTo(original)
    }
}