package dgs.software.classicchess.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PlayerIndicator(
    isWhite: Boolean,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier.size(30.dp)) {
        drawCircle(
            color = if (isWhite) Color.White else Color.Black,
            radius = size.minDimension / 2
        )
        drawCircle(
            color = if (isWhite) Color.Black else Color.White,
            radius = size.minDimension / 2,
            style = Stroke(width = 2f)
        )
    }
}