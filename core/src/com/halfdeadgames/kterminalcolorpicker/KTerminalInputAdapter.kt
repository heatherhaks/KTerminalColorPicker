package com.halfdeadgames.kterminalcolorpicker

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxInputAdapter

class KTerminalInputAdapter : KtxInputAdapter {
    var selector = Vector2(0f, 0f)
    val foreground = Color()
    val background = Color()

    private var modifier = 0f

    override fun keyUp(keycode: Int): Boolean {
        when(keycode) {
            Input.Keys.W -> selector.y -= if(selector.y.toInt() == 0) -7 else 1
            Input.Keys.S -> selector.y += if(selector.y.toInt() == 7) -7 else 1
            Input.Keys.A -> selector.x -= if(selector.x.toInt() == 0) -3 else 1
            Input.Keys.D -> selector.x += if(selector.x.toInt() == 3) -3 else 1
            Input.Keys.UP -> {
                setModifier(1f)
                setColor()
            }
            Input.Keys.DOWN -> {
                setModifier(-1f)
                setColor()
            }
        }
        return true
    }

    private fun setColor() {
        when(selector.y.toInt()) {
            0 -> foreground.r = bound(foreground.r, modifier, 0f, 1f)
            1 -> foreground.g = bound(foreground.g, modifier, 0f, 1f)
            2 -> foreground.b = bound(foreground.b, modifier, 0f, 1f)
            3 -> foreground.a = bound(foreground.a, modifier, 0f, 1f)
            4 -> background.r = bound(background.r, modifier, 0f, 1f)
            5 -> background.g = bound(background.g, modifier, 0f, 1f)
            6 -> background.b = bound(background.b, modifier, 0f, 1f)
            7 -> background.a = bound(background.a, modifier, 0f, 1f)
        }
    }

    private fun setModifier(amount: Float){
        modifier = when(selector.x.toInt()) {
            0 -> 1f * amount
            1 -> 0.1f * amount
            2 -> 0.01f * amount
            else -> 0.001f * amount
        }
    }



    private fun bound(value: Float, amount: Float , min: Float, max: Float) : Float {
        var output = value + amount

        if(output < min) output = min
        if(output > max) output = max

        return output
    }
}