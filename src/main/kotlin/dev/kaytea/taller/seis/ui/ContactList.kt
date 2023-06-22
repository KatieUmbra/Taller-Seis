package dev.kaytea.taller.seis.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import dev.kaytea.taller.seis.io.Contact
import io.kanro.compose.jetbrains.expui.control.Label

object ContactList {
    private fun findShortName(name: String): String {
        var text = name.split(" ")[0]
        if (text.length > 16) text = text.substring(0, 16)
        return text
    }

    @Composable
    fun contactList(contactList: SnapshotStateList<Contact>, selectedContact: MutableState<Contact?>) {
        Box(Modifier.width(200.dp).background(Color.DarkGray, shape = RoundedCornerShape(5.dp))) {
            Box(Modifier.fillMaxSize()) {
                LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
                    items(contactList) {
                        ContactElement(it.name, contactList, selectedContact)
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().background(Color.Transparent),
                    adapter = rememberScrollbarAdapter(scrollState = rememberLazyListState(0))
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ContactElement(name: String, contactList: SnapshotStateList<Contact>, selectedContact: MutableState<Contact?>) {
        TooltipArea(
            tooltip = {
                Box(
                    Modifier
                        .background(
                            Color.Gray,
                            shape = RoundedCornerShape(10)
                        )
                        .padding(2.5.dp)
                ) {
                    Label(
                        text = name,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            },
            modifier = Modifier,
            delayMillis = 600,
            tooltipPlacement = TooltipPlacement.CursorPoint(
                alignment = Alignment.BottomEnd,
                offset = DpOffset.Zero
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.0.dp)
                    .padding(2.5.dp)
                    .background(Color.Gray, RoundedCornerShape(10))
                    .clickable(true) {
                        selectedContact.value = contactList.find { it.name == name }!!
                    },
                contentAlignment = Alignment.Center
            ) {
                Label(
                    text = findShortName(name),
                    fontSize = 1.em,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}