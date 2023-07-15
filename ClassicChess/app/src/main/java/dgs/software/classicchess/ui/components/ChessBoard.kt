package dgs.software.classicchess.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.ui.theme.boardCellBlack
import dgs.software.classicchess.ui.theme.boardCellWhite
import dgs.software.classicchess.ui.theme.selectedCellColor

@Composable
fun ChessBoard(
    cellSelected: (Coordinate) -> Unit,
    game: Game,
    possibleMovesForSelectedPiece: List<RevertableMove>,
    kingInCheck: Coordinate?,
    selectedCoordinate: Coordinate?,
    boardDisplayedInverted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val rowIndices = if (boardDisplayedInverted) {
            listOf(7, 6, 5, 4, 3, 2, 1, 0)
        } else {
            listOf(0, 1, 2, 3, 4, 5, 6, 7)
        }

        rowIndices.forEach { rowIndex ->
            ChessRow(
                rowIndex = rowIndex,
                cellSelected = cellSelected,
                game = game,
                possibleMovesForSelectedPiece = possibleMovesForSelectedPiece,
                kingInCheck = kingInCheck,
                selectedCoordinate = selectedCoordinate,
                boardDisplayedInverted = boardDisplayedInverted,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ChessRow(
    rowIndex: Int,
    cellSelected: (Coordinate) -> Unit,
    game: Game,
    possibleMovesForSelectedPiece: List<RevertableMove>,
    kingInCheck: Coordinate?,
    selectedCoordinate: Coordinate?,
    boardDisplayedInverted: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        val colIndices = if (boardDisplayedInverted) {
            listOf(7, 6, 5, 4, 3, 2, 1, 0)
        } else {
            listOf(0, 1, 2, 3, 4, 5, 6, 7)
        }
        colIndices.forEach { colIndex ->
            ChessCell(
                colIndex = colIndex,
                rowIndex = rowIndex,
                cellSelected = cellSelected,
                game = game,
                possibleMovesForSelectedPiece = possibleMovesForSelectedPiece,
                kingInCheck = kingInCheck,
                selectedCoordinate = selectedCoordinate,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ChessCell(
    colIndex: Int,
    rowIndex: Int,
    cellSelected: (Coordinate) -> Unit,
    game: Game,
    possibleMovesForSelectedPiece: List<RevertableMove>,
    kingInCheck: Coordinate?,
    selectedCoordinate: Coordinate?,
    modifier: Modifier = Modifier
) {
    val curCoordinate = Coordinate(rowIndex, colIndex)

    val piece = game.board.get(rowIndex, colIndex)

    val interactionSource = MutableInteractionSource()
    val backgroundColor =
        if (selectedCoordinate == curCoordinate) {
            MaterialTheme.colors.selectedCellColor
        } else if (getCellBackgroundType(rowIndex, colIndex)) {
            MaterialTheme.colors.boardCellWhite
        } else {
            MaterialTheme.colors.boardCellBlack
        }

    Box(modifier = modifier
        .fillMaxSize()
        .background(backgroundColor)
        .wrapContentSize()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            cellSelected(Coordinate(rowIndex, colIndex))
        }) {
        if (piece != null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                if (possibleMovesForSelectedPiece.any { it.toPos == curCoordinate }
                    || kingInCheck == (curCoordinate)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        tint = MaterialTheme.colors.selectedCellColor
                    )
                }
                Icon(
                    painter = painterResource(id = getIconId(piece)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    tint = Color.Unspecified
                )
            }
        } else if (possibleMovesForSelectedPiece.any { it.toPos == curCoordinate }) {
            Text(
                text = "\u2B24",
                fontSize = 20.sp,
                color = MaterialTheme.colors.selectedCellColor
            )
        }
    }
}