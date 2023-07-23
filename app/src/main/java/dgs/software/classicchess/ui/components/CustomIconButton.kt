package dgs.software.classicchess.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Button(
        modifier = modifier
            .height(50.dp)
            .padding(5.dp),
        onClick = { onClick() },
        enabled = isEnabled
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription
        )
    }
}