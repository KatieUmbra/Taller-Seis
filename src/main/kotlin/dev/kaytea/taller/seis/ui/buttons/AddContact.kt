package dev.kaytea.taller.seis.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
import io.kanro.compose.jetbrains.expui.control.TextField
import io.kanro.compose.jetbrains.expui.theme.DarkTheme

object AddContact {

    private fun buttonModifier(action: () -> Unit): Modifier {
        return Modifier
            .clickable(enabled = true, onClick = action)
            .padding(2.5.dp)
            .background(DarkTheme.Grey6, RoundedCornerShape(10))
    }

    private var errorList: List<String> = listOf()
    private val errorWindowSize = DpSize(650.dp, 450.dp)
    private val windowSize = DpSize(600.dp, 400.dp)
    private var dialogOpen = mutableStateOf(false)
    private var errorDialogOpen = mutableStateOf(false)

    @Composable
    fun addContact(contactList: SnapshotStateList<Contact>) {
        var isDialogOpen by remember { dialogOpen }
        val isErrorDialogOpen by remember { errorDialogOpen }
        Box(buttonModifier {
            isDialogOpen = true
        }) {
            if (isDialogOpen) addContactDialog(contactList)
            if (isErrorDialogOpen) addErrorDialog()
            Label("Añadir Contacto", modifier = Modifier.padding(10.dp), fontSize = 1.em)
        }
    }

    @Composable
    fun addContactDialog(contactList: SnapshotStateList<Contact>) {
        var isDialogOpen by remember { dialogOpen }
        Dialog(
            onCloseRequest = { isDialogOpen = false },
            state = rememberDialogState(
                position = WindowPosition(Alignment.Center),
                size = windowSize
            ),
            title = "Nuevo contacto",
            resizable = false
        ) {
            DarkTheme {
                Column(
                    Modifier
                        .background(DarkTheme.Grey2)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var name by remember { mutableStateOf("") }
                    var number by remember { mutableStateOf("") }

                    var nameError by remember { mutableStateOf(false) }
                    var numberError by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.height((windowSize.height / 4) * 3).width(windowSize.width),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        val textFieldModifier = Modifier
                            .width((windowSize.width / 2) * 0.8f)
                            .height(windowSize.height / 8)
                            .background(DarkTheme.Grey6)
                        val textFieldTitleModifier = Modifier
                            .width((windowSize.width / 2) * 0.8f)
                            .height(windowSize.height / 12)
                        val textFieldTitleFontSize = 1.5.em
                        val textFieldPlaceholderModifier = Modifier
                            .background(Color.Transparent)

                        Column(
                            modifier = Modifier.height((windowSize.height / 4) * 3).width(windowSize.width / 2),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Label(
                                text = "Nombre",
                                modifier = textFieldTitleModifier,
                                fontSize = textFieldTitleFontSize,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = name,
                                modifier = textFieldModifier,
                                onValueChange = { it: String ->
                                    name = it
                                    nameError = Contact.verifyName(it) == null
                                },
                                isError = nameError,
                                textStyle = TextStyle(fontSize = 1.25.em),
                                placeholder = {
                                    Label(
                                        text = "Introduce un nombre",
                                        modifier = textFieldPlaceholderModifier,
                                        fontSize = 1.em,
                                        color = DarkTheme.Grey4
                                    )
                                }
                            )
                        }
                        Column(
                            modifier = Modifier.height((windowSize.height / 4) * 3).width(windowSize.width / 2),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Label(
                                "Numero",
                                modifier = textFieldTitleModifier,
                                fontSize = textFieldTitleFontSize,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = number,
                                modifier = textFieldModifier,
                                onValueChange = { it: String ->
                                    number = it
                                    numberError = Contact.verifyNumber(it) == null
                                },
                                placeholder = {
                                    Label(
                                        text = "Introduce un numero de teléfono",
                                        modifier = textFieldPlaceholderModifier,
                                        fontSize = 1.em,
                                        color = DarkTheme.Grey4
                                    )
                                },
                                isError = numberError,
                                textStyle = TextStyle(fontSize = 1.25.em)
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(windowSize.height / 4)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        var isErrorDialogOpen by remember { errorDialogOpen }
                        Box(buttonModifier {
                            contactList.clear()
                            contactList.addAll(updateContacts())
                            val localErrorList = mutableListOf<String>()
                            var success = true

                            val verifiedName = Contact.verifyName(name)
                            val verifiedNumber = Contact.verifyNumber(number)
                            if (verifiedName.isNullOrEmpty()) {
                                success = false
                                localErrorList.add(
                                    "Error! El nombre tiene mal formato\n" +
                                            "  • El nombre no puede ni empezar ni terminar en espacio.\n" +
                                            "  • El nombre no puede tener dos espacios consecutivos.\n" +
                                            "  • El nombre debe tener entre 4 y 35 letras.\n" +
                                            "  • El nombre no puede estar vació.\n" +
                                            "  • El nombre no puede tener caracteres especiales,\n" +
                                            "    solo letras o los siguientes caracteres: '-' '_' ' '"
                                )
                            }
                            if (verifiedNumber.isNullOrEmpty()) {
                                success = false
                                localErrorList.add(
                                    "Error! El numero tiene mal formato\n" +
                                            "  • El numero solo puede tener numeros de 0 a 9.\n" +
                                            "  • El numero debe ser de exactamente 10 digitos.\n" +
                                            "  • El numero no puede estar vació."
                                )
                            }
                            if (success) {
                                val verifyExistence = Contact(verifiedName!!, verifiedNumber!!)
                                if (verifyExistence in contactList) success = false
                                localErrorList.add("Error! Ese contacto ya existe.")
                            }
                            if (success) {
                                if (contactList.find { it.number === verifiedNumber } != null) {
                                    success = false
                                    localErrorList.add("Error! Ya existe un contacto con ese numero.")
                                }
                            }
                            if (success) {
                                val newContactList = Contacts(
                                    contactList
                                        .toMutableList()
                                        .also { it.add(Contact(verifiedName!!, verifiedNumber!!)) }
                                )
                                setContactList(newContactList)
                                contactList.clear()
                                contactList.addAll(updateContacts())
                                isDialogOpen = false
                            } else {
                                errorList = localErrorList
                                isErrorDialogOpen = true
                            }
                        }
                            .width((windowSize.width / 2) * 0.8f)
                            .fillMaxHeight(),
                            contentAlignment = Alignment.Center) {
                            Label("Submit", fontSize = 1.em, textAlign = TextAlign.Center)
                        }
                        Box(buttonModifier {
                            isDialogOpen = false
                        }
                            .width((windowSize.width / 2) * 0.8f)
                            .fillMaxHeight(),
                            contentAlignment = Alignment.Center) {
                            Label("Cancel", fontSize = 1.em, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun addErrorDialog() {
        var isDialogOpen by remember { errorDialogOpen }
        Dialog(
            onCloseRequest = { isDialogOpen = false },
            state = rememberDialogState(
                position = WindowPosition(Alignment.Center),
                size = errorWindowSize
            )
        ) {
            Column(
                modifier = Modifier
                    .background(DarkTheme.Grey2)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    for (error in errorList) {
                        Label(text = error, fontSize = 1.25.em)
                        Spacer(Modifier.height(16.dp))
                    }
                }
                Box {
                    Box(
                        modifier = buttonModifier { isDialogOpen = false }
                            .width(errorWindowSize.width * 0.6f)
                            .height(errorWindowSize.height * 0.15f)
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Label("Ok", fontSize = 3.em)
                    }
                }
            }
        }
    }
}