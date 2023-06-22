package dev.kaytea.taller.seis.io

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.encodeToString
import okio.FileSystem

fun setContactList(list: Contacts) {
    FileSystem.SYSTEM.write(contactsPath) {
        var allContactsAreValid = true
        for (contact in list.contactList) {
            if (Contact.verifyName(contact.name) == null) allContactsAreValid = false
            if (Contact.verifyNumber(contact.number) == null) allContactsAreValid = false
        }
        if (!allContactsAreValid) {
            println("Can't update. Invalid contact exists.")
            return
        }
        val serializedContacts = Yaml.default.encodeToString(list)
        writeUtf8(serializedContacts)
    }
}