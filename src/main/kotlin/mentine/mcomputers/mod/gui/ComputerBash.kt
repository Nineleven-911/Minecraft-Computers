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
import kotlin.math.max
import kotlin.math.min


@Environment(EnvType.CLIENT)
class ComputerBash : Screen(Text.of("Mentine OS")) {

    companion object {
        val debug = true
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
            return loadable.load(mc.resourceManager)
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
    private var current: StringBuilder = StringBuilder()

    override fun shouldPause() = false

    override fun init() {
        super.init()
        texts.add("")  // Initialize with an empty line.
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        if (cursorRow >= texts.size) {
            texts.add("")
        }
        current.insert(cursorCol, chr)
        cursorCol++

        texts[cursorRow] = current.toString()  // Update the current row's text
        return super.charTyped(chr, modifiers)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        when (keyCode) {
            GLFW.GLFW_KEY_ENTER -> handleEnter()
            GLFW.GLFW_KEY_BACKSPACE -> handleBackSpace()
            GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT -> handleMove(keyCode)
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private fun handleMove(keyCode: Int) {
        when (keyCode) {
            GLFW.GLFW_KEY_LEFT -> {
                if (cursorCol > 0) cursorCol--
            }
            GLFW.GLFW_KEY_RIGHT -> {
                if (cursorCol < current.length) cursorCol++
            }
        }
    }

    private fun handleBackSpace() {
        if (cursorCol > 0) {
            current.deleteCharAt(cursorCol - 1)
            cursorCol--
        } else if (cursorRow > 0) {
            val previousLine = texts[cursorRow - 1]
            cursorCol = previousLine.length
            texts[cursorRow - 1] = previousLine + texts[cursorRow]
            current = StringBuilder(texts[cursorRow - 1])
            texts.removeAt(cursorRow)
            cursorRow--
        }
        texts[cursorRow] = current.toString()  // Update the current row after backspace
    }

    private fun handleEnter() {
        texts.add("")  // Add a new line when pressing Enter
        cursorRow++
        cursorCol = 0
        current = StringBuilder()  // Reset current text for the new line
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (debug) {
            context.drawText(renderer, "Mouse X: $mouseX", 0, 10, WHITE, false)
            context.drawText(renderer, "Mouse Y: $mouseY", 0, 20, WHITE, false)
            context.drawText(renderer, "Delta: $delta", 0, 30, WHITE, false)
            context.drawText(renderer, "row: $cursorRow", 0, 40, WHITE, false)
            context.drawText(renderer, "col: $cursorCol", 0, 50, WHITE, false)
        }

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

        // 绘制 String
        for (i in 0 until texts.size) {
            val line = texts[i]
            context.drawText(
                renderer,
                line,
                x + 19, y + 18 + (i * 10), 0xFFFFFF, false
            )
        }

        // Draw the cursor
        if ((System.currentTimeMillis() / 500) % 2 == 0L) {
            val cursorX = x + 19 + getMCTextRenderer().getWidth(current.toString().take(cursorCol))
            val cursorY = y + 19 + (cursorRow * 10)
            context.fill(cursorX, cursorY, cursorX + 1, cursorY + 10, WHITE)
        }
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        return super.mouseScrolled(mouseX, mouseY, amount)
    }
}
