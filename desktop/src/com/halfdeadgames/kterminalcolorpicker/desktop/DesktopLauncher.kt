package com.halfdeadgames.kterminalcolorpicker.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.halfdeadgames.kterminalcolorpicker.*

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = "${TITLE} v${VERSION}"
        config.width = V_WIDTH
        config.height = V_HEIGHT
        config.backgroundFPS = FPS
        config.foregroundFPS = FPS
        config.resizable = RESIZEABLE
        LwjglApplication(KTerminalColorPicker(), config)
    }
}
