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
            { Text(text = stringResource(R.string.Dialog_OkText)) }
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
            { Text(text = stringResource(R.string.Dialog_OkText)) }
        },
        title = { Text(text = stringResource(R.string.GameOverDialog_StalemateText)) },
        text = { Text(text = "$playerString ${stringResource(R.string.GameOverDialog_StalemateDescriptionText)}") }
    )
}

@Composable
fun ResetGameDialog(
    onYesButtonClicked: () -> Unit,
    onNoButtonClicked: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onNoButtonClicked,
        confirmButton = {
            TextButton(onClick = onYesButtonClicked)
            { Text(text = stringResource(R.string.Yes_Answer)) }
        },
        dismissButton = {
            TextButton(onClick = onNoButtonClicked)
            { Text(text = stringResource(R.string.No_Answer)) }
        },
        title = { Text(text = stringResource(R.string.ResetGameDialog_Header)) },
        text = { Text(text = stringResource(R.string.ResetGameDialog_Text)) }
    )
}