package mentine.mcomputers

import mentine.mcomputers.mod.item.ModItem
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object MinecraftComputers : ModInitializer {
    const val MOD_ID = "mcomputers"
    val logger = LoggerFactory.getLogger(MOD_ID)!!

	override fun onInitialize() {
        ModItem.onRegister()
	}
}