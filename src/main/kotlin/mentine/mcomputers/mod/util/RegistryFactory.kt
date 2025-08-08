package mentine.mcomputers.mod.util

import mentine.mcomputers.MinecraftComputers.MOD_ID
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

fun<T: Item> register(name: String, item: T): T {
    Registry.register(Registries.ITEM, "$MOD_ID:$name", item)
    return item
}

fun<T: Block> register(name: String, block: T): T {
    Registry.register(Registries.BLOCK, "$MOD_ID:$name", block)
    return block
}
