package io.github.jedlimlx.supplemental_patches.platforms.neoforge

//? neoforge {
import com.mojang.blaze3d.platform.InputConstants
import io.github.jedlimlx.supplemental_patches.MOD_ID
import io.github.jedlimlx.supplemental_patches.PLATFORM
import io.github.jedlimlx.supplemental_patches.platforms.neoforge.KeyBinds.KB_REGENERATE_SHADERS
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.minecraft.client.KeyMapping
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

@EventBusSubscriber(modid = MOD_ID, value = [Dist.CLIENT])
object KeyBinds {
    val KB_REGENERATE_SHADERS by lazy {
        KeyMapping(
            "key.supplemental_patches.reload_shaders",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_INSERT,
            "key.categories.misc"
        )
    }

    @SubscribeEvent
	@JvmStatic
    fun registerBindings(event: RegisterKeyMappingsEvent) {
        event.register(KB_REGENERATE_SHADERS)
    }
}

@EventBusSubscriber(modid = MOD_ID, value = [Dist.CLIENT])
object KeyHandler {
    @SubscribeEvent
	@JvmStatic
    fun onClientTick(event: ClientTickEvent.Post) {
        while (KB_REGENERATE_SHADERS.consumeClick())
            PLATFORM.sendSystemMessage(installShader())
    }
}
//?}
