package mentine.mcomputers.mod.commands

import mentine.mcomputers.MinecraftComputers
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager.literal

object ModCommand {
    fun onRegister() {
        CommandRegistrationCallback.EVENT.register { dispatcher, commandRegistryAccess, environment ->
            dispatcher.register(literal("mcomputers").executes {
                MinecraftComputers.logger.info("Mentine OS")
                1
            })
        }
    }
}
