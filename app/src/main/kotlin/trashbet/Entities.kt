@file:Suppress("EXPERIMENTAL_API_USAGE", "unused")

package trashbet

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.descriptors.SerialDescriptor
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

@Serializable
data class User(@Serializable(with = UUIDSerializer::class) val id: UUID? = null, val name: String)

object Users: UUIDTable() {
    val name = varchar("name", 255)
    val amount = integer("amount")

    fun toUser(row: ResultRow): User = User(
        id = row[id].value,
        name = row[name],
    )
}

object Bets: UUIDTable() {
    val description = text("description")
    val complete = bool("complete")
}

object Wager: UUIDTable() {
    val amount = integer("amount")
    val user = reference("user", Users)
    val bet = reference("bet", Bets)
}

@ExperimentalSerializationApi
@Serializer(forClass = UUID::class)
    object UUIDSerializer : KSerializer<UUID> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: UUID) {
            encoder.encodeString(value.toString())
        }

        override fun deserialize(decoder: Decoder): UUID {
            return UUID.fromString(decoder.decodeString())
        }
    }
