package mentine.mcomputers.mod.gui

import mentine.mcomputers.MinecraftComputers.MOD_ID
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.Font
import net.minecraft.client.font.FontStorage
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.font.TrueTypeFontLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.io.IOException


@Environment(EnvType.CLIENT)
class ComputerBash() : Screen(Text.of("Mentine OS")) {

    companion object {
        private fun getMCFont(): TextRenderer {
            val mc: MinecraftClient = MinecraftClient.getInstance()
            val list: MutableList<Font> = mutableListOf()
            val loader = TrueTypeFontLoader(
                Identifier("mcomputers:mc.ttf"),
                16f,
                20f,
                TrueTypeFontLoader.Shift.NONE,
                ""
            )
            val loadable = loader.build().orThrow()
            val font: Font = loadable.load(mc.resourceManager)
            list.add(font)
            val storage = FontStorage(mc.textureManager, Identifier("mcomputers:mcfont"))
            storage.setFonts(list)
            return TextRenderer({ storage }, true)
        }
        val renderer = getMCFont()
        val computerShell = Identifier.of(MOD_ID, "textures/gui/computer_bash.png")!!
        const val SHELL_WIDTH = 351
        const val SHELL_HEIGHT = 249
    }

    private var cursor = 0
    private val texts: MutableList<String> = mutableListOf()

    override fun shouldPause() = false

    override fun init() {
        super.init()
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        return super.charTyped(chr, modifiers)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Set vals
        val (x, y) = (width - SHELL_WIDTH) / 2 to (height - SHELL_HEIGHT) / 2
        // 绘制灰色半透明覆盖层
        val alpha = (255 * 0.4).toInt() // 40% 透明度
        val grayColor = 0x808080 or (alpha shl 24) // 灰色加上alpha通道
        context.fill(0, 0, width, height, grayColor)
        // 绘制界面
        context.drawTexture(
            computerShell,
            x, y, 0f, 0f,
            SHELL_WIDTH, SHELL_HEIGHT,
            SHELL_WIDTH, SHELL_HEIGHT
        )
        // 绘制 String
        context.drawText(
            renderer,
            "Hello, World!",
            x + 19, y + 18, 0xFFFFFF, false
        )
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        return super.mouseScrolled(mouseX, mouseY, amount)
    }
}
