package dev.kaytea.taller.seis.io

import okio.FileSystem
import okio.Path.Companion.toPath

val homeDirectory = System.getProperty("user.home").toPath()
val contactsPath = homeDirectory.resolve(".kontacts/").resolve("contacts.yaml")

object Setup {
    fun setupIO() {
        val dirPath = homeDirectory.resolve(".kontacts/")
        val contactsPath = dirPath.resolve("contacts.yaml")

        val dirExists = FileSystem.SYSTEM.exists(dirPath)
        val contactsExist = FileSystem.SYSTEM.exists(contactsPath)

        if (!dirExists) FileSystem.SYSTEM.createDirectories(dirPath)
        if (!contactsExist) FileSystem.SYSTEM.write(contactsPath) {
            writeUtf8("")
        }
    }
}
