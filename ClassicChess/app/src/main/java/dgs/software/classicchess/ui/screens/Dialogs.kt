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
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss)
            { Text(text = stringResource(R.string.GameWonDialog_OkText)) }
        },
        title = { Text(text = stringResource(R.string.GameWonDialog_GameOverText)) },
        text = { Text(text = " ${stringResource(R.string.GameWonDialog_PlayerWonText)}") }
    )
}