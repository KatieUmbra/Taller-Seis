package dev.kaytea.taller.seis.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import dev.kaytea.taller.seis.io.Contact
import dev.kaytea.taller.seis.io.Contacts
import dev.kaytea.taller.seis.io.setContactList
import dev.kaytea.taller.seis.io.updateContacts
import io.kanro.compose.jetbrains.expui.control.Label
import io.kanro.compose.jetbrains.expui.theme.DarkTheme

object DeleteContact {

    private fun buttonModifier(action: () -> Unit): Modifier {
        return Modifier
            .clickable(enabled = true, onClick = action)
            .padding(2.5.dp)
            .background(DarkTheme.Grey6, RoundedCornerShape(10))
    }

    @Composable
    fun deleteContact(contactList: SnapshotStateList<Contact>, selectedContact: MutableState<Contact?>) {
        val isPromptOpen = remember { mutableStateOf(false) }
        Box(Modifier
            .padding(2.5.dp)
            .background(
                if (selectedContact.value == null) DarkTheme.Grey3 else DarkTheme.Grey6,
                RoundedCornerShape(10)
            )
            .clickable(enabled = selectedContact.value != null) {
                if (selectedContact.value != null) isPromptOpen.value = true
            }
            .width((900.dp / 4) * 0.8f)
            .height(700.dp / 16),
            contentAlignment = Alignment.Center
        ) {
            if (isPromptOpen.value) addDeletePrompt(contactList, selectedContact, isPromptOpen)
            Label("Eliminar Contacto", modifier = Modifier.padding(10.dp), fontSize = 1.em)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun addDeletePrompt(
        contactList: SnapshotStateList<Contact>,
        selectedContact: MutableState<Contact?>,
        isPromptOpen: MutableState<Boolean>
    ) {
        val windowSize = DpSize(400.dp, 250.dp)
        Dialog(
            title = "Eliminar Contacto",
            resizable = false,
            onCloseRequest = { isPromptOpen.value = false },
            state = rememberDialogState(
                position = WindowPosition(Alignment.Center),
                size = windowSize
            ),
            onKeyEvent = {
                if (it.key == Key.Escape && it.type == KeyEventType.KeyDown && isPromptOpen.value) {
                    isPromptOpen.value = false
                }
                false
            }
        ) {
            DarkTheme {
                Column(
                    Modifier
                        .background(DarkTheme.Grey2)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Label(
                        text = "Esta segurx que desea eliminar este contacto?\n" +
                                "(una vez hecho no se va a poder recuperar).",
                        modifier = Modifier.width((windowSize.width) * 0.8f),
                        textAlign = TextAlign.Center,
                        fontSize = 1.1.em
                    )
                    Row(
                        modifier = Modifier.height(windowSize.height / 2)
                            .width(windowSize.width),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(buttonModifier {
                            contactList.clear()
                            contactList.addAll(updateContacts())
                            val oldSelected = selectedContact.value!!.copy()
                            val oldContacts = contactList.toList()
                            selectedContact.value = null
                            val newContacts = Contacts(oldContacts.toMutableList().also { it.remove(oldSelected) })
                            setContactList(newContacts)
                            contactList.clear()
                            contactList.addAll(updateContacts())
                            isPromptOpen.value = false
                        }
                            .height((windowSize.height) * 0.2f)
                            .width((windowSize.width / 2) * 0.8f),
                            contentAlignment = Alignment.Center)
                        {
                            Label(
                                text = "Si",
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                fontSize = 1.5.em
                            )
                        }
                        Box(buttonModifier { isPromptOpen.value = false }
                            .height((windowSize.height) * 0.2f)
                            .width((windowSize.width / 2) * 0.8f),
                            contentAlignment = Alignment.Center)
                        {
                            Label(
                                text = "No",
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                fontSize = 1.5.em
                            )
                        }
                    }
                }
            }
        }
    }
}