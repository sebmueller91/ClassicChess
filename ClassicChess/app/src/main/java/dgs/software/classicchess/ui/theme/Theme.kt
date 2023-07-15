package dgs.software.classicchess.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Colors.boardCellWhite: Color
    get() = MutedGold

val Colors.boardCellBlack: Color
    get() = DarkOliveGreen

val Colors.boardBorderColor: Color
    get() = Black

val Colors.selectedCellColor: Color
    get() = BrickRed

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = BlueGray,
    onPrimary = White,
    primaryVariant = Blue900,
    secondary = RegularGray,
    onSecondary = Color.White,
    surface = Color.DarkGray,
    onSurface = Color.White,
    background = DarkBlueishGray,
    onBackground = SoftCyan
)

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = LightColorPalette

@Composable
fun ClassicChessTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}