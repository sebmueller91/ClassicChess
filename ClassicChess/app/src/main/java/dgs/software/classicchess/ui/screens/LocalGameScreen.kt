package dgs.software.classicchess.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.ui.theme.boardCellBlack
import dgs.software.classicchess.ui.theme.boardCellWhite
import dgs.software.classicchess.ui.theme.selectedCellColor

@Composable
fun LocalGameScreen(
    viewModel: LocalGameViewModel,
    modifier: Modifier = Modifier
) {
    LocalGameScreen(
        uiStateFlow = viewModel.uiState.collectAsState().value, // TODO: Change to stateWithLifecycle
        resetPlayerWon = viewModel::resetPlayerWon,
        resetPlayerStalemate = viewModel::resetPlayerStalemate,
        resetGame = viewModel::resetGame,
        dismissPromotePawn = viewModel::dismissPromotePawn,
        promotePawn = viewModel::promotePawn,
        invertBoardDisplayDirection = viewModel::invertBoardDisplayDirection,
        undoPreviousMove = viewModel::undoPreviousMove,
        redoNextMove = viewModel::redoNextMove,
        cellSelected = viewModel::cellSelected,
        modifier = modifier
    )
}

@Composable
fun LocalGameScreen(
    uiStateFlow: LocalGameUiState,
    resetPlayerWon: () -> Unit,
    resetPlayerStalemate: () -> Unit,
    resetGame: () -> Unit,
    dismissPromotePawn: () -> Unit,
    promotePawn: (Type) -> Unit,
    invertBoardDisplayDirection: () -> Unit,
    undoPreviousMove: () -> Unit,
    redoNextMove: () -> Unit,
    cellSelected: (Coordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    uiStateFlow.playerWon?.let {
        GameWonDialog(player = it) {
            resetPlayerWon()
        }
    }
    uiStateFlow.playerStalemate?.let {
        StalemateDialog(player = it) {
            resetPlayerStalemate()
        }
    }

    var showResetConfirmationDialog by remember { mutableStateOf(false) }
    if (showResetConfirmationDialog) {
        ResetGameDialog(
            onYesButtonClicked = {
                resetGame()
                showResetConfirmationDialog = false
            },
            onNoButtonClicked = { showResetConfirmationDialog = false })
    }


    if (uiStateFlow.requestPawnPromotionInput) {
        PromotePawnDialog(
            onDismiss = dismissPromotePawn,
            onPlayerChoice = promotePawn
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${stringResource(R.string.LocalGameScreen_CurrentPlayerText)}" + "   ")
            PlayerIndicator(isWhiteTurn = uiStateFlow.game.currentPlayer == Player.WHITE)
        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .aspectRatio(1f)
                .border(
                    3.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RectangleShape
                )
                .padding(2.dp)
                .fillMaxWidth()
        ) {
            ChessBoard(
                uiStateFlow,
                cellSelected,
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                onClick = invertBoardDisplayDirection,
                isEnabled = true,
                iconId = R.drawable.ic_baseline_invertupdown_24,
                contentDescription = stringResource(R.string.LocalGameScreen_InvertButtonContentDescription)
            )
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .padding(5.dp),
                onClick = { showResetConfirmationDialog = true },
                enabled = uiStateFlow.canResetGame
            ) {
                Text(
                    text = stringResource(R.string.LocalGameScreen_ResetButtonText)
                )
            }
            IconButton(
                onClick = undoPreviousMove,
                isEnabled = uiStateFlow.canUndoMove,
                iconId = R.drawable.ic_baseline_undo_24,
                contentDescription = stringResource(R.string.LocalGameScreen_UndoButtonContentDescription)
            )
            IconButton(
                onClick = redoNextMove,
                isEnabled = uiStateFlow.canRedoMove,
                iconId = R.drawable.ic_baseline_redo_24,
                contentDescription = stringResource(R.string.LocalGameScreen_RedoButtonContentDescription)
            )
        }
    }
}

@Composable
fun ChessBoard(
    uiStateFlow: LocalGameUiState,
    cellSelected: (Coordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val rowIndices = if (uiStateFlow.boardDisplayedInverted) {
            listOf(7, 6, 5, 4, 3, 2, 1, 0)
        } else {
            listOf(0, 1, 2, 3, 4, 5, 6, 7)
        }
        ChessRow(rowIndices[0], uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessRow(rowIndices[1], uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessRow(rowIndices[2], uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessRow(rowIndices[3], uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessRow(rowIndices[4], uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessRow(rowIndices[5], uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessRow(rowIndices[6], uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessRow(rowIndices[7], uiStateFlow, cellSelected, Modifier.weight(1f))
    }
}

@Composable
fun ChessRow(
    colIndex: Int,
    uiStateFlow: LocalGameUiState,
    cellSelected: (Coordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        val colIndices = if (uiStateFlow.boardDisplayedInverted) {
            listOf(7, 6, 5, 4, 3, 2, 1, 0)
        } else {
            listOf(0, 1, 2, 3, 4, 5, 6, 7)
        }
        ChessCell(colIndices[0], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessCell(colIndices[1], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessCell(colIndices[2], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessCell(colIndices[3], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessCell(colIndices[4], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessCell(colIndices[5], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessCell(colIndices[6], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
        ChessCell(colIndices[7], colIndex, uiStateFlow, cellSelected, Modifier.weight(1f))
    }
}

@Composable
fun ChessCell(
    colIndex: Int,
    rowIndex: Int,
    uiStateFlow: LocalGameUiState,
    cellSelected: (Coordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    val curCoordinate = Coordinate(rowIndex, colIndex)

    val piece = uiStateFlow.game.board.get(rowIndex, colIndex)

    val interactionSource = MutableInteractionSource()
    val backgroundColor =
        if (uiStateFlow.selectedCoordinate == curCoordinate) {
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
                if (uiStateFlow.possibleMovesForSelectedPiece.any { it.toPos == curCoordinate }
                    || uiStateFlow.kingInCheck == (curCoordinate)) {
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
        } else if (uiStateFlow.possibleMovesForSelectedPiece.any { it.toPos == curCoordinate }) {
            Text(
                text = "\u2B24",
                fontSize = 20.sp,
                color = MaterialTheme.colors.selectedCellColor
            )
        }
    }
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Button(
        modifier = modifier
            .height(50.dp)
            .padding(5.dp),
        onClick = { onClick() },
        enabled = isEnabled
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun PlayerIndicator(
    isWhiteTurn: Boolean,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier.size(30.dp)) {
        drawCircle(
            color = if (isWhiteTurn) Color.White else Color.Black,
            radius = size.minDimension / 2
        )
        drawCircle(
            color = if (isWhiteTurn) Color.Black else Color.White,
            radius = size.minDimension / 2,
            style = Stroke(width = 2f)
        )
    }
}

private fun getIconId(piece: Piece): Int {
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

// returns true if it is a cell with light background, false otherwise
private fun getCellBackgroundType(rowIndex: Int, colIndex: Int): Boolean {
    return (rowIndex % 2 == 0 && colIndex % 2 == 0) || (rowIndex % 2 != 0 && colIndex % 2 != 0)
}

@Composable
@Preview
private fun PreviewLocalGameScreen() {
    LocalGameScreen(
        uiStateFlow = LocalGameUiState(), resetPlayerWon = { },
        resetPlayerStalemate = { },
        resetGame = { },
        dismissPromotePawn = { },
        promotePawn = { },
        invertBoardDisplayDirection = { },
        undoPreviousMove = { },
        redoNextMove = { },
        cellSelected = { },
    )
}