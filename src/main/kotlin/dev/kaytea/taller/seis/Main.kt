package dev.kaytea.taller.seis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.kaytea.taller.seis.io.Contact
import dev.kaytea.taller.seis.io.Setup
import dev.kaytea.taller.seis.io.updateContacts
import dev.kaytea.taller.seis.ui.ContactInfo
import dev.kaytea.taller.seis.ui.ContactList
import dev.kaytea.taller.seis.ui.buttons.AddContact
import dev.kaytea.taller.seis.ui.buttons.DeleteContact
import dev.kaytea.taller.seis.ui.buttons.UpdateContact
import io.kanro.compose.jetbrains.expui.control.Label
import io.kanro.compose.jetbrains.expui.theme.DarkTheme
import io.kanro.compose.jetbrains.expui.window.JBWindow

@Composable
fun mainElement(selectedContact: MutableState<Contact?>) {
    val padding = 16.dp
    val contactList = remember { mutableStateListOf<Contact>() }
    contactList.addAll(updateContacts())
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(DarkTheme.Grey2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Label("Kontacts", fontSize = 1.5.em)
        Label("Aplicación de contactos escrita en Kotlin", fontSize = 0.75.em)
        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(Modifier.fillMaxHeight().width(400.dp), contentAlignment = Alignment.Center) {
                ContactInfo.contactInfo(selectedContact)
            }
            ContactList.contactList(contactList, selectedContact)
            Column(
                Modifier.fillMaxHeight().width(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                AddContact.addContact(contactList)
                Spacer(Modifier.height(16.dp))
                DeleteContact.deleteContact(contactList, selectedContact)
                Spacer(Modifier.height(16.dp))
                UpdateContact.updateContact(contactList, selectedContact)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val selectedContact = remember { mutableStateOf<Contact?>(null) }
    Setup.setupIO()
    JBWindow(
        title = "Taller 6",
        resizable = false,
        state = rememberWindowState(size = DpSize(900.dp, 700.dp)),
        onCloseRequest = { exitApplication() },
        theme = DarkTheme,
        showTitle = true,
        alwaysOnTop = true,
        onKeyEvent = {
            if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                selectedContact.value = null
            }
            false
        }
    ) {
        DarkTheme {
            mainElement(selectedContact)
        }
    }
}