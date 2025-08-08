package mentine.mcomputers.mod.item

import mentine.mcomputers.MinecraftComputers.logger
import mentine.mcomputers.mod.gui.ComputerBash
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class HandheldComputer(settings: Settings) : Item(settings) {
    constructor() : this(FabricItemSettings())

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            return TypedActionResult.fail(user.getStackInHand(hand))
        }
        MinecraftClient.getInstance().setScreenAndRender(ComputerBash())
        logger.info("You have just used a Handheld Computer!")
        return TypedActionResult.success(user.getStackInHand(hand))
    }
}
