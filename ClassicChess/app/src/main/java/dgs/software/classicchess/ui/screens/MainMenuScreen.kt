package dgs.software.classicchess.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun MainMenuScreen(
    onLocalGameButtonClickedAction: () -> Unit,
    onComputerGameButtonClickedAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onLocalGameButtonClickedAction
        ) {
            Text(
                text = stringResource(dgs.software.classicchess.R.string.MainMenuScreen_LocalGameButtonText)
            )
        }
        Button(
            onClick = onComputerGameButtonClickedAction,
            enabled = false
        ) {
            Text(
                text = stringResource(dgs.software.classicchess.R.string.MainMenuScreen_ComputerGameButtonText)
            )
        }
    }
}