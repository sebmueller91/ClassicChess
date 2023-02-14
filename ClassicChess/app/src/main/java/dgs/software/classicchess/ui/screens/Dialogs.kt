package dgs.software.classicchess.ui.screens

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Player

@Composable
fun GameWonDialog(
    player: Player,
    onDismiss: () -> Unit
) {
    val playerString = when (player) {
        Player.WHITE -> stringResource(R.string.WhitePlayer)
        Player.BLACK -> stringResource(R.string.BlackPlayer)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss)
            { Text(text = stringResource(R.string.GameWonDialog_OkText)) }
        },
        title = { Text(text = stringResource(R.string.GameWonDialog_GameOverText)) },
        text = { Text(text = "$playerString ${stringResource(R.string.GameWonDialog_PlayerWonText)}") }
    )
}

@Composable
fun StalemateDialog(
    player: Player,
    onDismiss: () -> Unit
) {
    val playerString = when (player) {
        Player.WHITE -> stringResource(R.string.WhitePlayer)
        Player.BLACK -> stringResource(R.string.BlackPlayer)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss)
            { Text(text = stringResource(R.string.GameWonDialog_OkText)) } // TODO
        },
        title = { Text(text = stringResource(R.string.GameWonDialog_GameOverText)) },
        text = { Text(text = "$playerString ${stringResource(R.string.GameWonDialog_PlayerWonText)}") }
    )
}

@Composable
fun ResetGameDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm)
            { Text(text = "Yes") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss)
            { Text(text = "No") }
        },
        title = { Text(text = stringResource(R.string.GameWonDialog_GameOverText)) },
        text = { Text(text = "Do you really want to reset the current game?") },
    )
}