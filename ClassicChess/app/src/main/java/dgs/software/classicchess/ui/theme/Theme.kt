package dgs.software.classicchess.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Colors.boardCellWhite: Color
    get() = if (isLight) Yellow100 else Yellow800

val Colors.boardCellBlack: Color
    get() = if (isLight) Green400 else Green900

val Colors.boardBorderColor: Color
    get() = if (isLight) Gray900 else Gray400

val Colors.selectedCellColor: Color
    get() = if (isLight) Orange400 else Blue700

private val LightColorPalette = lightColors(
    primary = Blue600,
    primaryVariant = Gray900,
    secondary = Gray900,
    surface = White
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val DarkColorPalette = darkColors(
    primary = Blue900,
    onPrimary = Gray100,
    primaryVariant = Gray900,
    secondary = Blue600,
    surface = Black
)

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