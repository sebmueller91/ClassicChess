package dgs.software.classicchess.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.ui.theme.selectedCellColor
import dgs.software.classicchess.ui.theme.boardBorderColor
import dgs.software.classicchess.ui.theme.boardCellBlack
import dgs.software.classicchess.ui.theme.boardCellWhite

@Composable
fun LocalGameScreen(
    viewModel: LocalGameViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text("Current Player: " + viewModel.gameUiState.currentPlayer)
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .background(MaterialTheme.colors.boardBorderColor)
        ) {
            ChessBoard(
                viewModel,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(2.dp)
                    .fillMaxWidth()
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .padding(5.dp),
                onClick = { viewModel.invertBoardDisplayDirection() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_reverseupdown_24),
                    contentDescription = null
                )
            }
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .padding(5.dp),
                onClick = {  },
                enabled = viewModel.canResetGame()
            ) {
                Text(
                    text = "Reset"
                )
            }
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .padding(5.dp),
                onClick = { viewModel.undoLastMove() },
                enabled = viewModel.gameUiState.canUndoMove
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_undo_24),
                    contentDescription = null
                )
            }
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .padding(5.dp),
                onClick = { viewModel.redoNextMove() },
                enabled = viewModel.gameUiState.canRedoMove
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_redo_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ChessBoard(
    viewModel: LocalGameViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val rowIndices = if (viewModel.boardDisplayedInverted) {
            listOf(7,6,5,4,3,2,1,0)
        } else {
            listOf(0, 1, 2, 3, 4, 5, 6, 7)
        }
        ChessRow(rowIndices[0], viewModel, Modifier.weight(1f))
        ChessRow(rowIndices[1], viewModel, Modifier.weight(1f))
        ChessRow(rowIndices[2], viewModel, Modifier.weight(1f))
        ChessRow(rowIndices[3], viewModel, Modifier.weight(1f))
        ChessRow(rowIndices[4], viewModel, Modifier.weight(1f))
        ChessRow(rowIndices[5], viewModel, Modifier.weight(1f))
        ChessRow(rowIndices[6], viewModel, Modifier.weight(1f))
        ChessRow(rowIndices[7], viewModel, Modifier.weight(1f))
    }
}

@Composable
fun ChessRow(
    colIndex: Int,
    viewModel: LocalGameViewModel,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        val colIndices = if (viewModel.boardDisplayedInverted) {
            listOf(7,6,5,4,3,2,1,0)
        } else {
            listOf(0, 1, 2, 3, 4, 5, 6, 7)
        }
        ChessCell(colIndices[0], colIndex, viewModel, Modifier.weight(1f))
        ChessCell(colIndices[1], colIndex, viewModel, Modifier.weight(1f))
        ChessCell(colIndices[2], colIndex, viewModel, Modifier.weight(1f))
        ChessCell(colIndices[3], colIndex, viewModel, Modifier.weight(1f))
        ChessCell(colIndices[4], colIndex, viewModel, Modifier.weight(1f))
        ChessCell(colIndices[5], colIndex, viewModel, Modifier.weight(1f))
        ChessCell(colIndices[6], colIndex, viewModel, Modifier.weight(1f))
        ChessCell(colIndices[7], colIndex, viewModel, Modifier.weight(1f))
    }
}

@Composable
fun ChessCell(
    colIndex: Int,
    rowIndex: Int,
    viewModel: LocalGameViewModel,
    modifier: Modifier = Modifier
) {
    val curCoordinate = Coordinate(rowIndex, colIndex)
    val context = LocalContext.current

    val cell = viewModel.gameUiState.getBoard().get(rowIndex, colIndex)
    val isPiece = !(cell is Cell.Empty)

    val interactionSource = MutableInteractionSource()
    val backgroundColor =
        if (viewModel.selectedCell?.coordinate == curCoordinate) {
            MaterialTheme.colors.selectedCellColor
        } else if (viewModel.getCellBackgroundType(rowIndex, colIndex)) {
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
            viewModel.cellSelected(Coordinate(rowIndex, colIndex))
        }) {
        if (isPiece) {
            Box(
                Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.possibleMovesForSelectedPiece.any { it.toPos == curCoordinate }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        tint = MaterialTheme.colors.selectedCellColor
                    )
                }
                Icon(
                    painter = painterResource(id = getIconId(cell as Cell.Piece)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    tint = Color.Unspecified
                )
            }
        } else if (viewModel.possibleMovesForSelectedPiece.any { it.toPos == curCoordinate }) {
            Text(
                text = "\u2B24",
                fontSize = 20.sp,
                color = MaterialTheme.colors.selectedCellColor
            )
        }
    }
}

private fun getIconId(piece: Cell.Piece): Int {
    return when (piece.player) {
        Player.WHITE -> when (piece.type) {
            Type.PAWN -> R.drawable.pawn_white
            Type.ROOK -> R.drawable.rook_white
            Type.KNIGHT -> R.drawable.knight_white
            Type.BISHOP -> R.drawable.bishop_white
            Type.QUEEN -> R.drawable.queen_white
            Type.KING -> R.drawable.king_white
        }
        Player.BLACK -> when (piece.type) {
            Type.PAWN -> R.drawable.pawn_black
            Type.ROOK -> R.drawable.rook_black
            Type.KNIGHT -> R.drawable.knight_black
            Type.BISHOP -> R.drawable.bishop_black
            Type.QUEEN -> R.drawable.queen_black
            Type.KING -> R.drawable.king_black
        }
    }
}