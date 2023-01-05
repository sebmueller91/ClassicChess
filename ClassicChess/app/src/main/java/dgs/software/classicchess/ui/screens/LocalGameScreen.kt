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
import dgs.software.classicchess.model.Board
import dgs.software.classicchess.R

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
        BoardColumn(board, 0, Modifier.weight(1f))
        BoardColumn(board, 1, Modifier.weight(1f))
        BoardColumn(board, 2, Modifier.weight(1f))
        BoardColumn(board, 3, Modifier.weight(1f))
        BoardColumn(board, 4, Modifier.weight(1f))
        BoardColumn(board, 5, Modifier.weight(1f))
        BoardColumn(board, 6, Modifier.weight(1f))
        BoardColumn(board, 7, Modifier.weight(1f))
    }
}

@Composable
fun BoardColumn(
    board: Board,
    colIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Cell(board, 0, colIndex, Modifier.weight(1f))
        Cell(board, 1, colIndex, Modifier.weight(1f))
        Cell(board, 2, colIndex, Modifier.weight(1f))
        Cell(board, 3, colIndex, Modifier.weight(1f))
        Cell(board, 4, colIndex, Modifier.weight(1f))
        Cell(board, 5, colIndex, Modifier.weight(1f))
        Cell(board, 6, colIndex, Modifier.weight(1f))
        Cell(board, 7, colIndex, Modifier.weight(1f))
    }
}

@Composable
fun Cell(
    board: Board,
    rowIndex: Int,
    colIndex: Int,
    modifier: Modifier = Modifier
) {
    val interactionSource = MutableInteractionSource()
    Box(modifier = modifier
        .fillMaxSize()
        .background(getCellBackgroundColor(rowIndex, colIndex))
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {}) {
        Icon(
            painter = painterResource(id = R.drawable.rook_black),
            contentDescription = null
        )
//        Button(
//            onClick = { /*TODO*/ },
//            border = BorderStroke(1.dp, Color.Black),
//            shape = RoundedCornerShape(0.dp),
//            modifier = Modifier.clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null
//            ) {
//                /* .... */
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
//        ) {
//            Text(
//                text = "A",
//                modifier = Modifier.fillMaxSize()
//            )
//        }

    }
}

private fun getCellBackgroundColor(rowIndex: Int, colIndex: Int): Color {
    if ((rowIndex % 2 == 0 && colIndex % 2 == 0) || (rowIndex % 2 != 0 && colIndex % 2 != 0)) {
        return Color.LightGray
    } else {
        return Color.DarkGray
    }
}