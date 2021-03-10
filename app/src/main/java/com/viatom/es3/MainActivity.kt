package com.viatom.es3

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class MainActivity : AppCompatActivity() {
    lateinit var mGLSurfaceView: GLSurfaceView
    private fun bindRenderer(): GLSurfaceView.Renderer? {
        return CubeRenderer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        mGLSurfaceView = findViewById(R.id.gl)
        val renderer = bindRenderer()
        mGLSurfaceView.run {
            setEGLContextClientVersion(3)
            setRenderer(renderer)
        }
    }

}