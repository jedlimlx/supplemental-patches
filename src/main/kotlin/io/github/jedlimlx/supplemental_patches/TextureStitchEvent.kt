package io.github.jedlimlx.supplemental_patches

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.renderer.texture.TextureAtlas

fun interface TextureStitchEvent {
    fun afterStitched(texture: TextureAtlas)

    companion object {
        @JvmStatic
        val EVENT = EventFactory.createArrayBacked(
            TextureStitchEvent::class.java
        ) { listeners: Array<TextureStitchEvent> ->
            TextureStitchEvent { texture ->
                listeners.forEach { it.afterStitched(texture) }
            }
        }
    }
}