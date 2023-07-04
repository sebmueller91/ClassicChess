package dgs.software.classicchess.ui.screens

data class GameMode(
    val name: String,
    val description: String,
    val iconId: Int,
    val buttonClickedAction: () -> Unit
)