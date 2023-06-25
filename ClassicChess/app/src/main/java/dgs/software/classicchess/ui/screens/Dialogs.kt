package dgs.software.classicchess.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dgs.software.classicchess.R
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type

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

// TODO: Move string ressources into R
@Composable
fun PromotePawnDialog(
    onDismiss: () -> Unit,
    onPlayerChoice: (Type) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        val queen = stringResource(R.string.Queen)
        val knight = stringResource(R.string.Knight)
        val rook = stringResource(R.string.Rook)
        val bishop = stringResource(R.string.Bishop)

        val options = listOf(queen, knight, rook, bishop)
        var selected by remember { mutableStateOf(queen) }
        val setSelected = { selection: String -> selected = selection }
        val choiceAsType: (String) -> Type =
            { name ->
                when (name) {
                    queen -> {
                        Type.QUEEN
                    }
                    knight -> {
                        Type.KNIGHT
                    }
                    rook -> {
                        Type.ROOK
                    }
                    else -> {
                        Type.BISHOP
                    }
                }
            }

        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(30.dp),
            elevation = 8.dp
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                Text(
                    text = stringResource(R.string.PromotePawnDialog_Header),
                    fontWeight = Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(12.dp)
                        .padding(end = 30.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    options.forEach { item ->
                        Row(verticalAlignment = CenterVertically) {
                            RadioButton(
                                selected = selected == item,
                                onClick = { setSelected(item) },
                                enabled = true,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colors.primary
                                )
                            )
                            Text(
                                text = item,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable { setSelected(item) })
                        }
                    }
                }
                Row(Modifier.padding(12.dp)) {
                    Button(
                        onClick = { onPlayerChoice(choiceAsType(selected)) },
                        Modifier.padding(12.dp)

                    ) {
                        Text(stringResource(R.string.PromotePawnDialog_AcceptText))
                    }
                }
            }

        }
    }

}