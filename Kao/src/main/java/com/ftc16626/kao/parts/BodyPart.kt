package com.ftc16626.kao.parts

import android.graphics.Canvas
import android.graphics.Paint

interface BodyPart {
    var x: Float
    var y: Float

    val paint: Paint

    fun draw(canvas: Canvas)
}
