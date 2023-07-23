package dgs.software.classicchess.ui.screens.local_game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.ui.components.ChessBoard
import dgs.software.classicchess.ui.components.CustomIconButton
import dgs.software.classicchess.ui.components.PlayerIndicator
import dgs.software.classicchess.ui.screens.GameWonDialog
import dgs.software.classicchess.ui.screens.PromotePawnDialog
import dgs.software.classicchess.ui.screens.ResetGameDialog
import dgs.software.classicchess.ui.screens.StalemateDialog

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
            titleId = R.string.ResetGameDialog_Header,
            textId = R.string.ResetGameDialog_Text,
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
            Text(text = "${stringResource(R.string.CurrentPlayerText)}" + "   ")
            PlayerIndicator(isWhite = uiStateFlow.game.currentPlayer == Player.WHITE)
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
                modifier = Modifier.fillMaxSize(),
                boardDisplayedInverted = uiStateFlow.boardDisplayedInverted,
                cellSelected = cellSelected,
                game = uiStateFlow.game,
                kingInCheck = uiStateFlow.kingInCheck,
                possibleMovesForSelectedPiece = uiStateFlow.possibleMovesForSelectedPiece,
                selectedCoordinate = uiStateFlow.selectedCoordinate
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            CustomIconButton(
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
            CustomIconButton(
                onClick = undoPreviousMove,
                isEnabled = uiStateFlow.canUndoMove,
                iconId = R.drawable.ic_baseline_undo_24,
                contentDescription = stringResource(R.string.UndoButtonContentDescription)
            )
            CustomIconButton(
                onClick = redoNextMove,
                isEnabled = uiStateFlow.canRedoMove,
                iconId = R.drawable.ic_baseline_redo_24,
                contentDescription = stringResource(R.string.RedoButtonContentDescription)
            )
        }
    }
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