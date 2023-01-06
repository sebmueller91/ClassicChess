package dgs.software.classicchess.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type

@Composable
fun LocalGameScreen(
    localGameViewModel: LocalGameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(30.dp)
            .background(Color.Black)
    ) {
        ChessBoard(
            localGameViewModel.gameUiState.getBoard(),
            modifier = Modifier
                .aspectRatio(1f)
                .padding(2.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun ChessBoard(
    board: Board,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        ChessColumn(board, 0, Modifier.weight(1f))
        ChessColumn(board, 1, Modifier.weight(1f))
        ChessColumn(board, 2, Modifier.weight(1f))
        ChessColumn(board, 3, Modifier.weight(1f))
        ChessColumn(board, 4, Modifier.weight(1f))
        ChessColumn(board, 5, Modifier.weight(1f))
        ChessColumn(board, 6, Modifier.weight(1f))
        ChessColumn(board, 7, Modifier.weight(1f))
    }
}

@Composable
fun ChessColumn(
    board: Board,
    colIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ChessCell(board, 0, colIndex, Modifier.weight(1f))
        ChessCell(board, 1, colIndex, Modifier.weight(1f))
        ChessCell(board, 2, colIndex, Modifier.weight(1f))
        ChessCell(board, 3, colIndex, Modifier.weight(1f))
        ChessCell(board, 4, colIndex, Modifier.weight(1f))
        ChessCell(board, 5, colIndex, Modifier.weight(1f))
        ChessCell(board, 6, colIndex, Modifier.weight(1f))
        ChessCell(board, 7, colIndex, Modifier.weight(1f))
    }
}

@Composable
fun ChessCell(
    board: Board,
    rowIndex: Int,
    colIndex: Int,
    modifier: Modifier = Modifier
) {
    val cell = board.get(rowIndex, colIndex)
    val isPiece = !(cell is Cell.Empty)

    val interactionSource = MutableInteractionSource()
    Box(modifier = modifier
        .fillMaxSize()
        .background(getCellBackgroundColor(rowIndex, colIndex))
        .wrapContentSize()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {}) {
        if (isPiece) {
            Icon(
                painter = painterResource(id = getIconId(cell as Cell.Piece)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                tint = Color.Unspecified
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

private fun getCellBackgroundColor(rowIndex: Int, colIndex: Int): Color {
    if ((rowIndex % 2 == 0 && colIndex % 2 == 0) || (rowIndex % 2 != 0 && colIndex % 2 != 0)) {
        return Color.LightGray
    } else {
        return Color.DarkGray
    }
}