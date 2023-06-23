package dev.kaytea.taller.seis.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
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

object UpdateContact {

    private fun buttonModifier(action: () -> Unit): Modifier {
        return Modifier
            .clickable(enabled = true, onClick = action)
            .padding(2.5.dp)
            .background(DarkTheme.Grey6, RoundedCornerShape(10))
    }

    private val windowSize = DpSize(600.dp, 400.dp)
    private val confirmationWindowSize = DpSize(600.dp, 400.dp)
    private val errorWindowSize = DpSize(650.dp, 450.dp)

    private val errorList = mutableListOf("")
    private lateinit var updatedContact: Contact

    @Composable
    fun updateContact(contactList: SnapshotStateList<Contact>, selectedContact: MutableState<Contact?>) {
        val isDialogOpen = remember { mutableStateOf(false) }
        val isConfirmationDialogOpen = remember { mutableStateOf(false) }
        val isErrorDialogOpen = remember { mutableStateOf(false) }
        if (isDialogOpen.value) updateContactDialog(
            contactList,
            selectedContact,
            isDialogOpen,
            isConfirmationDialogOpen,
            isErrorDialogOpen
        )
        if (isConfirmationDialogOpen.value) updateConfirmationDialog(
            contactList,
            selectedContact,
            isConfirmationDialogOpen,
            isDialogOpen
        )
        if (isErrorDialogOpen.value) updateErrorDialog(
            isErrorDialogOpen
        )
        Box(Modifier
            .padding(2.5.dp)
            .background(
                if (selectedContact.value == null) DarkTheme.Grey3 else DarkTheme.Grey6,
                RoundedCornerShape(10)
            )
            .clickable(enabled = selectedContact.value != null) {
                if (selectedContact.value != null) isDialogOpen.value = true
            }
            .width((900.dp / 4) * 0.8f)
            .height(700.dp / 16),
            contentAlignment = Alignment.Center
        ) {
            Label("Actualizar Contacto", modifier = Modifier.padding(10.dp), fontSize = 1.em)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun updateContactDialog(
        contactList: SnapshotStateList<Contact>,
        selectedContact: MutableState<Contact?>,
        isDialogOpen: MutableState<Boolean>,
        isConfirmationDialogOpen: MutableState<Boolean>,
        isErrorDialogOpen: MutableState<Boolean>
    ) {
        Dialog(
            title = "Nuevo contacto",
            resizable = false,
            onCloseRequest = { isDialogOpen.value = false },
            state = rememberDialogState(
                position = WindowPosition(Alignment.Center),
                size = windowSize
            ),
            onKeyEvent = {
                if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                    isDialogOpen.value = false
                }
                true
            }
        ) {
            DarkTheme {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(DarkTheme.Grey2)
                        .fillMaxSize(),
                ) {
                    var name by remember { mutableStateOf(selectedContact.value!!.name) }
                    var number by remember { mutableStateOf(selectedContact.value!!.number) }

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
                        Box(buttonModifier {
                            contactList.clear()
                            contactList.addAll(updateContacts())
                            errorList.clear()
                            var success = true
                            if (nameError) {
                                success = false
                                errorList.add(
                                    "Error! El nombre tiene mal formato\n" +
                                            "  • El nombre no puede ni empezar ni terminar en espacio.\n" +
                                            "  • El nombre no puede tener dos espacios consecutivos.\n" +
                                            "  • El nombre debe tener entre 4 y 35 letras.\n" +
                                            "  • El nombre no puede estar vació.\n" +
                                            "  • El nombre no puede tener caracteres especiales,\n" +
                                            "    solo letras o los siguientes caracteres: '-' '_' ' '"
                                )
                            }
                            if (numberError) {
                                success = false
                                errorList.add(
                                    "Error! El numero tiene mal formato\n" +
                                            "  • El numero solo puede tener números de 0 a 9.\n" +
                                            "  • El numero debe ser de exactamente 10 dígitos.\n" +
                                            "  • El numero no puede estar vació."
                                )
                            }
                            if (success) {
                                val verifyExistence = Contact(name, number)
                                val newList = contactList.toMutableList()
                                    .also { it.remove(selectedContact.value) }
                                if (verifyExistence in newList) {
                                    success = false
                                    errorList.add("Error! Ese contacto ya existe.")
                                }
                            }
                            if (success) {
                                val newList = contactList.toMutableList()
                                    .also { it.remove(selectedContact.value) }
                                if (newList.find { it.number == number } != null) {
                                    success = false
                                    errorList.add("Error! Ya existe otro contacto con ese numero.")
                                }
                            }
                            if (success) {
                                updatedContact = Contact(name, number)
                                isConfirmationDialogOpen.value = true
                            } else {
                                isErrorDialogOpen.value = true
                            }
                            contactList.clear()
                            contactList.addAll(updateContacts())
                        }
                            .width((windowSize.width / 2) * 0.8f)
                            .fillMaxHeight(),
                            contentAlignment = Alignment.Center) {
                            Label("Submit", fontSize = 1.em, textAlign = TextAlign.Center)
                        }
                        Box(buttonModifier {
                            isDialogOpen.value = false
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

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun updateConfirmationDialog(
        contactList: SnapshotStateList<Contact>,
        selectedContact: MutableState<Contact?>,
        isConfirmationDialogOpen: MutableState<Boolean>,
        isDialogOpen: MutableState<Boolean>
    ) {
        Dialog(
            title = "Eliminar Contacto",
            resizable = false,
            onCloseRequest = { isConfirmationDialogOpen.value = false },
            state = rememberDialogState(
                position = WindowPosition(Alignment.Center),
                size = confirmationWindowSize
            ),
            onKeyEvent = {
                if (it.key == Key.Escape && it.type == KeyEventType.KeyDown && isConfirmationDialogOpen.value) {
                    isConfirmationDialogOpen.value = false
                }
                false
            }
        ) {
            DarkTheme {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(DarkTheme.Grey2)
                        .fillMaxSize()
                ) {
                    Label(
                        textAlign = TextAlign.Center,
                        fontSize = 1.1.em,
                        modifier = Modifier.width((confirmationWindowSize.width) * 0.8f),
                        text = if (selectedContact.value == updatedContact) {
                            "No se han realizado cambios."
                        } else {
                            "Esta segurx que desea actualizar este contacto?\n" +
                                    "(una vez hecho no, el contacto anterior no se va a poder recuperar)."
                        }
                    )
                    Row(
                        modifier = Modifier.height(confirmationWindowSize.height / 2)
                            .width(confirmationWindowSize.width),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (selectedContact.value == updatedContact) {
                            Box(buttonModifier { isConfirmationDialogOpen.value = false }
                                .height((confirmationWindowSize.height) * 0.2f)
                                .width((confirmationWindowSize.width) * 0.8f),
                                contentAlignment = Alignment.Center) {
                                Label(
                                    text = "Ok",
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center,
                                    fontSize = 1.5.em
                                )
                            }
                        } else {
                            Box(buttonModifier {
                                contactList.clear()
                                contactList.addAll(updateContacts())
                                val newContactList = Contacts(
                                    contactList
                                        .toMutableList()
                                        .also { it.remove(selectedContact.value) }
                                        .also { it.add(updatedContact) }
                                )
                                setContactList(newContactList)
                                contactList.clear()
                                contactList.addAll(updateContacts())
                                contactList.clear()
                                contactList.addAll(updateContacts())
                                isConfirmationDialogOpen.value = false
                                isDialogOpen.value = false
                                selectedContact.value = null
                            }
                                .height((confirmationWindowSize.height) * 0.2f)
                                .width((confirmationWindowSize.width / 2) * 0.8f),
                                contentAlignment = Alignment.Center)
                            {
                                Label(
                                    text = "Si",
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center,
                                    fontSize = 1.5.em
                                )
                            }
                            Box(buttonModifier { isConfirmationDialogOpen.value = false }
                                .height((confirmationWindowSize.height) * 0.2f)
                                .width((confirmationWindowSize.width / 2) * 0.8f),
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

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun updateErrorDialog(
        isErrorDialogOpen: MutableState<Boolean>
    ) {
        Dialog(
            onCloseRequest = { isErrorDialogOpen.value = false },
            state = rememberDialogState(
                position = WindowPosition(Alignment.Center),
                size = errorWindowSize
            ),
            onKeyEvent = {
                if (it.key == Key.Escape && it.type == KeyEventType.KeyDown && isErrorDialogOpen.value)
                    isErrorDialogOpen.value = false
                false
            }
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
                        modifier = buttonModifier { isErrorDialogOpen.value = false }
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