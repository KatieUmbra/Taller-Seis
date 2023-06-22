package dev.kaytea.taller.seis.io

import com.charleskorn.kaml.EmptyYamlDocumentException
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import okio.FileSystem

fun updateContacts(): List<Contact> {
    FileSystem.SYSTEM.read(
        contactsPath
    ) {
        val contactsString = readUtf8()
        val contacts: Contacts = try {
            Yaml.default.decodeFromString<Contacts>(contactsString)
        } catch (_: EmptyYamlDocumentException) {
            Contacts(listOf())
        }
        return@updateContacts contacts.contactList
    }
}