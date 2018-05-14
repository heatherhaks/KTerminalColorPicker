package com.halfdeadgames.kterminalcolorpicker

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.halfdeadgames.kterminal.KTerminalData
import com.halfdeadgames.kterminal.KTerminalRenderer
import ktx.app.clearScreen
import ktx.app.use

class KTerminalColorPicker : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var terminalData: KTerminalData
    lateinit var terminalRenderer: KTerminalRenderer

    val terminalWidth = 35
    val terminalHeight = 26
    var exampleForegroundColor = Color.WHITE.cpy()
    var exampleBackgroundColor = Color.BLACK.cpy()

    var selector = Vector2(0f, 0f)

    val inputAdapter = KTerminalInputAdapter()
    override fun create() {
        batch = SpriteBatch()

        terminalData = KTerminalData(terminalWidth, terminalHeight)
        terminalRenderer = KTerminalRenderer("fontSheet.png", 1f, batch)

        inputAdapter.selector.set(selector)
        inputAdapter.foreground.set(exampleForegroundColor)
        inputAdapter.background.set(exampleBackgroundColor)
        Gdx.input.inputProcessor = inputAdapter
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)

        terminalData.clearAll()

        selector.set(inputAdapter.selector)
        exampleForegroundColor.set(inputAdapter.foreground)
        exampleBackgroundColor.set(inputAdapter.background)

        drawUI()

        batch.use {
            terminalRenderer.render(0, 0, terminalData)
        }
    }

    fun drawUI() {
        //border
        terminalData.resetCursor()

        terminalData[0, 0].drawBox( terminalWidth,
                                    terminalHeight,
                                    KTerminalData.BOX_DOUBLE_DOWN_RIGHT,
                                    KTerminalData.BOX_DOUBLE_DOWN_LEFT,
                                    KTerminalData.BOX_DOUBLE_UP_RIGHT,
                                    KTerminalData.BOX_DOUBLE_UP_LEFT,
                                    KTerminalData.BOX_DOUBLE_HORIZONTAL,
                                    KTerminalData.BOX_DOUBLE_VERTICAL)

        //divider
        val dividerX = terminalWidth - 16
        terminalData[dividerX, 1].drawLine(dividerX, terminalHeight - 2, KTerminalData.BOX_SINGLE_VERTICAL)
        terminalData[dividerX, 0].write(KTerminalData.BOX_DOWN_SINGLE_HORIZONTAL_DOUBLE)
        terminalData[dividerX, terminalHeight - 1].write(KTerminalData.BOX_UP_SINGLE_HORIZONTAL_DOUBLE)

        //example block
        val exampleX = 2
        val exampleY = 5
        terminalData[exampleForegroundColor, exampleBackgroundColor]
        var char = 0
        for(y in 0 until 16) {
            for(x in 0 until 16) {
                terminalData[x + exampleX, y + exampleY].write(char.toChar())
                char++
            }
        }

        //color info
        val digits = 3
        val colorDisplayY = 2
        val colorDisplayXOffset = 3
        terminalData[dividerX + colorDisplayXOffset, colorDisplayY][Color.WHITE, Color.BLACK].write("Foreground")
        terminalData[dividerX + colorDisplayXOffset, colorDisplayY + 1].drawLine(dividerX + colorDisplayXOffset + 9, colorDisplayY + 1, KTerminalData.BOX_SINGLE_HORIZONTAL)
        displayColors(dividerX + colorDisplayXOffset + 1, colorDisplayY + 2, digits, exampleForegroundColor)
        terminalData[dividerX + colorDisplayXOffset, colorDisplayY + 7][Color.WHITE, Color.BLACK].write("Background")
        terminalData[dividerX + colorDisplayXOffset, colorDisplayY + 8].drawLine(dividerX + colorDisplayXOffset + 9, colorDisplayY + 8, KTerminalData.BOX_SINGLE_HORIZONTAL)
        displayColors(dividerX + colorDisplayXOffset + 1, colorDisplayY + 9, digits, exampleBackgroundColor)

        //selector
        val selectorOffsetX: Int = if(selector.x.toInt() == 0) 0 else 1
        val selectorOffsetY: Int = if(selector.y.toInt() < 4) selector.y.toInt() + 2 else selector.y.toInt() + 5
        terminalData[dividerX + colorDisplayXOffset, colorDisplayY + selectorOffsetY][Color.YELLOW].write(KTerminalData.ANGLE_RIGHT)
        val colorValue: Float = when(selector.y.toInt()) {
            0 -> exampleForegroundColor.r
            1 -> exampleForegroundColor.g
            2 -> exampleForegroundColor.b
            3 -> exampleForegroundColor.a
            4 -> exampleBackgroundColor.r
            5 -> exampleBackgroundColor.g
            6 -> exampleBackgroundColor.b
            else -> exampleBackgroundColor.a}
        terminalData[dividerX + colorDisplayXOffset + 3 + selectorOffsetX + selector.x.toInt(), colorDisplayY + selectorOffsetY][Color.YELLOW].write(colorValue.format(digits)[selector.x.toInt() + selectorOffsetX])

        //help
        val helpX: Int = dividerX + ((terminalWidth - dividerX) / 2)
        val helpY: Int = terminalHeight - 9
        terminalData[dividerX, helpY - 1][Color.WHITE].write(KTerminalData.BOX_SINGLE_VERTICAL_RIGHT)
        terminalData[terminalWidth - 1, helpY - 1].write(KTerminalData.BOX_VERTICAL_DOUBLE_LEFT_SINGLE)
        terminalData[dividerX + 1, helpY - 1].drawLine(terminalWidth - 2, helpY -1, KTerminalData.BOX_SINGLE_HORIZONTAL)
        terminalData[helpX - 1, helpY][Color.YELLOW].write("W/S")
        terminalData[helpX - 6, helpY + 1][Color.WHITE].write("Select Field")
        terminalData[helpX - 1, helpY + 3][Color.YELLOW].write("A/D")
        terminalData[helpX - 6, helpY + 4][Color.WHITE].write("Select Digit")
        terminalData[helpX - 3, helpY + 6][Color.YELLOW].write("Up/Down")
        terminalData[helpX - 6, helpY + 7][Color.WHITE].write("Change Value")

        terminalData.resetCursor()
    }

    fun displayColors(x: Int, y: Int, digits: Int, color: Color) {
        terminalData[x, y].write("R:${color.r.format(digits)}")
        terminalData[x, y + 1].write("G:${color.g.format(digits)}")
        terminalData[x, y + 2].write("B:${color.b.format(digits)}")
        terminalData[x, y + 3].write("A:${color.a.format(digits)}")
    }

    override fun dispose() {
        batch.dispose()
    }
}
