package dgs.software.classicchess.ui.screens.computer_game

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
import androidx.compose.ui.unit.dp
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.ui.components.ChessBoard
import dgs.software.classicchess.ui.components.CustomIconButton
import dgs.software.classicchess.ui.components.PlayerIndicator
import dgs.software.classicchess.ui.screens.GameWonDialog
import dgs.software.classicchess.ui.screens.PromotePawnDialog
import dgs.software.classicchess.ui.screens.ResetGameDialog
import dgs.software.classicchess.ui.screens.StalemateDialog

@Composable
fun ComputerGameScreen(
    viewModel: ComputerGameViewModel,
    modifier: Modifier = Modifier
) {
    ComputerGameScreen(
        uiStateFlow = viewModel.uiState.collectAsState().value, // TODO: Change to stateWithLifecycle
        resetPlayerWon = viewModel::resetPlayerWon,
        resetPlayerStalemate = viewModel::resetPlayerStalemate,
        startNewGame = viewModel::startNewGame,
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
fun ComputerGameScreen(
    uiStateFlow: ComputerGameUiState,
    resetPlayerWon: () -> Unit,
    resetPlayerStalemate: () -> Unit,
    startNewGame: () -> Unit,
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

    var showNewGameConfirmationDialog by remember { mutableStateOf(false) }
    if (showNewGameConfirmationDialog) {
        ResetGameDialog(
            onYesButtonClicked = {
                startNewGame()
                showNewGameConfirmationDialog = false
            },
            onNoButtonClicked = { showNewGameConfirmationDialog = false })
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

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerIndicator(isWhite = uiStateFlow.computerPlayer == Player.WHITE)
                Text(text = "   " + "${stringResource(R.string.ComputerVsPlayer)}" + "   ")
                PlayerIndicator(isWhite = uiStateFlow.computerPlayer.opponent() == Player.WHITE)
            }
            Row(
                modifier = Modifier
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${stringResource(R.string.CurrentPlayerText)}" + "   ")
                PlayerIndicator(isWhite = uiStateFlow.game.currentPlayer == Player.WHITE)
            }
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
                selectedCoordinate = uiStateFlow.selectedCoordinate,
                lastComputerMove = uiStateFlow.lastComputerMove,
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
                onClick = { showNewGameConfirmationDialog = true },
                enabled = uiStateFlow.canStartNewGame
            ) {
                Text(
                    text = stringResource(R.string.ComputerGameScreen_NewGameButtonText)
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