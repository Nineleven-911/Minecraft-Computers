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
import org.lwjgl.glfw.GLFW
import kotlin.math.min


@Environment(EnvType.CLIENT)
class ComputerBash : Screen(Text.of("Mentine OS")) {

    companion object {
        private fun getMCFont(): Font {
            val mc: MinecraftClient = MinecraftClient.getInstance()
            val loader = TrueTypeFontLoader(
                Identifier("mcomputers:mc.ttf"),
                16f,
                20f,
                TrueTypeFontLoader.Shift.NONE,
                ""
            )
            val loadable = loader.build().orThrow()
            val font: Font = loadable.load(mc.resourceManager)
            return font
        }

        private fun getMCTextRenderer(): TextRenderer {
            val list: MutableList<Font> = mutableListOf()
            list.add(getMCFont())
            val storage = FontStorage(MinecraftClient.getInstance().textureManager, Identifier("mcomputers:mcfont"))
            storage.setFonts(list)
            return TextRenderer({ storage }, true)
        }
        val renderer = getMCTextRenderer()
        val computerShell = Identifier.of(MOD_ID, "textures/gui/computer_bash.png")!!
        const val WHITE = 0xffffff or (255 shl 24)
        const val GRAY = 0x808080 or ((255 * 0.4).toInt() shl 24)
        const val SHELL_WIDTH = 351
        const val SHELL_HEIGHT = 249
    }

    private var cursorRow = 0
    private var cursorCol = 0
    private val texts: MutableList<String> = mutableListOf()
    private var current : StringBuilder = StringBuilder()

    override fun shouldPause() = false

    override fun init() {
        super.init()
        texts.add(0, "")
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        current.append(chr)
        cursorRow++
        return super.charTyped(chr, modifiers)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            handleEnter()
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            handleBackSpace()
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private fun handleBackSpace() {
        if (cursorRow > 0 && current.isNotEmpty()) {
            current.deleteCharAt(current.lastIndex)
            cursorRow--
        }
    }

    private fun handleEnter() {
        texts.add("")
        cursorCol++
        current = StringBuilder()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Set vals
        val (x, y) = (width - SHELL_WIDTH) / 2 to (height - SHELL_HEIGHT) / 2
        // 绘制灰色半透明覆盖层
        context.fill(0, 0, width, height, GRAY)
        // 绘制界面
        context.drawTexture(
            computerShell,
            x, y, 0f, 0f,
            SHELL_WIDTH, SHELL_HEIGHT,
            SHELL_WIDTH, SHELL_HEIGHT
        )

        texts[cursorCol] = current.toString()   // TODO: Render不应该处理逻辑

        // 绘制 String
        for (i in 0 until  texts.size) {
            val line = texts[i]
            context.drawText(
                renderer,
                line,
                x + 19, y + 18 + (i * 10), 0xFFFFFF, false
            )
        }

        if ((System.currentTimeMillis() / 1000) % 2 == 0L){
            val cursorX = x + 19 + getMCTextRenderer().getWidth(texts[cursorCol])
            val cursorY = y + 19 + (cursorCol * 10)
            context.fill(cursorX, cursorY, cursorX + 1, cursorY + 10, WHITE)
        }
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        return super.mouseScrolled(mouseX, mouseY, amount)
    }
}
