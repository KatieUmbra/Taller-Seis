package dev.kaytea.taller.seis.io

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("contact")
data class Contact(
    val name: String,
    val number: String
) {
    companion object {
        @JvmStatic
        fun verifyNumber(number: String): String? {
            val regex = Regex("^[0-9]{10}$")
            return if (regex.matches(number)) number
            else null
        }

        @JvmStatic
        fun verifyName(name: String): String? {
            val regex = Regex("^(?! )(?!.* {2})(?!.* \$)[a-zA-Z-_ ]{4,25}\$")
            return if (regex.matches(name)) name
            else null
        }
    }
}

@Serializable
data class Contacts(
    val contactList: List<Contact>
)