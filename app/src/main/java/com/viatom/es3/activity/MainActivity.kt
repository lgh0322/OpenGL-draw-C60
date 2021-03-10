package com.viatom.es3.activity

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.viatom.es3.R
import com.viatom.es3.renderer.CubeRenderer
import java.lang.Thread.sleep

open class MainActivity : AppCompatActivity() {
    private lateinit var mGLSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sleep(1000)
        init()
    }

    private fun init() {
        mGLSurfaceView = findViewById(R.id.gl)
        val renderer = CubeRenderer()
        mGLSurfaceView.run {
            setEGLContextClientVersion(3)
            setRenderer(renderer)
        }
    }

}