package su.mandora.authlib

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable(with = AgentSerializer::class)
enum class Agent(val agentName: String, val version: Int) {
    MINECRAFT("Minecraft", 1),
    SCROLLS("Scrolls", 1) // (untested)
}

object AgentSerializer : KSerializer<Agent> {
    override val descriptor = PrimitiveSerialDescriptor("Agent", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Agent {
        // Technically not needed
        return decoder.decodeSerializableValue(JsonObject.serializer()).getOrDefault("name", Agent.MINECRAFT.agentName).let { Agent.entries.firstOrNull { agent -> agent.agentName == it } ?: Agent.MINECRAFT }
    }

    override fun serialize(encoder: Encoder, value: Agent) {
        encoder.encodeSerializableValue(JsonObject.serializer(), buildJsonObject {
            put("name", value.agentName)
            put("version", value.version)
        })
    }
}