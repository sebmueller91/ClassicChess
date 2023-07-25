package dgs.software.classicchess.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dgs.software.classicchess.BuildConfig
import dgs.software.classicchess.R
import dgs.software.classicchess.calculations.ai.Difficulty

@Composable
fun MainMenuScreen(
    startLocalGame: () -> Unit,
    startComputerGame: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectDifficultyDialogVisible by remember { mutableStateOf(false) }
    SelectDifficultyDialog(
        showDialog = selectDifficultyDialogVisible,
        onDifficultySelected = { difficulty -> startComputerGame(difficulty) }) {
        selectDifficultyDialogVisible = false
    }

    val gameModes = listOf(
        GameMode(
            iconId = R.drawable.playervsplayer,
            name = stringResource(R.string.MainMenu_LocalGame),
            description = stringResource(R.string.MainMenu_PlayFriendDescription),
            buttonClickedAction = startLocalGame
        ),
        GameMode(
            iconId = R.drawable.playervscomputer,
            name = stringResource(R.string.MainMenu_PlayComputer),
            description = stringResource(R.string.MainMenu_PlayComputerDescription),
            buttonClickedAction = { selectDifficultyDialogVisible = true }
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val imageAspectRatio = 1536f / 768f

        Column(
            Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)) {
            Image(
                painter = painterResource(id = R.drawable.mainappimage),
                contentDescription = "App Logo",
                modifier = Modifier
                    .requiredSize(width = screenWidth, height = screenWidth / imageAspectRatio)
                    .padding(top = 24.dp),
                contentScale = ContentScale.Crop
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(gameModes) { index, gameMode ->
                    GameModeCard(
                        gameMode = gameMode,
                        imageSize = 80.dp,
                        modifier = modifier.height(140.dp)
                    )
                    if (index < gameModes.size - 1) {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 50.dp)
                                .background(MaterialTheme.colors.onBackground)
                        )
                    }
                }
            }
        }
        val infoText = "App version: ${BuildConfig.VERSION_NAME}\n©2023 Sebastian Müller\ngithub.com/sebmueller91"
        Text(
            text = infoText,
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun GameModeCard(
    gameMode: GameMode,
    imageSize: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { gameMode.buttonClickedAction() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = gameMode.iconId),
                contentDescription = "${gameMode.name} Icon",
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    gameMode.name,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    gameMode.description,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}
