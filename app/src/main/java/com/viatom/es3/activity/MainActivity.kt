package com.viatom.es3.activity

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.viatom.es3.R
import com.viatom.es3.renderer.CubeRenderer

open class MainActivity : AppCompatActivity() {
    private lateinit var mGLSurfaceView: GLSurfaceView
    private lateinit var renderer: CubeRenderer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

//    val x = event.x
//    val y = event.y
//    when (event.action)
//    {
//        MotionEvent.ACTION_MOVE -> {
//        var dx = x - previousX
//        var dy = y - previousY
//        renderer.angle += dx / 100
//        mGLSurfaceView.requestRender()
//    }
//
//    }
//    previousX = x
//    previousY = y

    @SuppressLint("ClickableViewAccessibility")
    val gaga = View.OnTouchListener { _, event ->
        val x = event!!.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x - previousX
                var dy = y - previousY
                renderer.angleX = (dx / 5)
                renderer.angleY = (dy / 5)
                mGLSurfaceView.requestRender()
            }

        }
        previousX = x
        previousY = y
        true
    }

    private fun init() {
        mGLSurfaceView = findViewById(R.id.gl)

        renderer = CubeRenderer()
        mGLSurfaceView.run {
            setOnTouchListener(gaga)
            setEGLContextClientVersion(3)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }
    }

}