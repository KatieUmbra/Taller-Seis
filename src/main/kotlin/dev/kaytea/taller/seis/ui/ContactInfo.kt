package dev.kaytea.taller.seis.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import dev.kaytea.taller.seis.io.Contact
import io.kanro.compose.jetbrains.expui.control.Label
import io.kanro.compose.jetbrains.expui.theme.DarkTheme

object ContactInfo {
    private fun extractColor(number: String): Color {
        val red = (number.substring(0, 3).toInt() * 346277346) % 255
        val green = (number.substring(3, 6).toInt() * 7895683) % 255
        val blue = (number.substring(6, 9).toInt() * 12345945) % 255
        val alpha = 255
        return Color(red, green, blue, alpha)
    }

    @Composable
    fun contactInfo(selectedUser: MutableState<Contact?>) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(550.dp)
                .background(
                    DarkTheme.Grey3,
                    shape = RoundedCornerShape(5.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedUser.value != null) {
                val color = extractColor(selectedUser.value!!.number)
                Box(Modifier.height(350.dp).width(350.dp).padding(32.dp)) {
                    Image(
                        painter = painterResource("person.svg"),
                        contentDescription = "Image of a person with default attributes",
                        modifier = Modifier.fillMaxSize().background(color, shape = RoundedCornerShape(40.dp)),
                    )
                }
                val labelModifier = Modifier
                    .width(400.dp * 0.8f)
                    .height(32.dp)
                    .background(DarkTheme.Grey4)
                Label(
                    text = "\uD83D\uDC64 " + selectedUser.value!!.name,
                    modifier = labelModifier,
                    fontSize = TextUnit(1.25f, TextUnitType.Em),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Label(
                    "\uD83D\uDCDE (+57) " + selectedUser.value!!.number,
                    modifier = labelModifier,
                    fontSize = TextUnit(1.25f, TextUnitType.Em),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}