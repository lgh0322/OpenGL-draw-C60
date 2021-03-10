package com.viatom.es3

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class MainActivity : AppCompatActivity() {
    private var mGLSurfaceView: GLSurfaceView? = null

   private fun bindRenderer(): GLSurfaceView.Renderer? {
        return IndicesCubeRenderer()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCore.getInstance().init(application)
        setupViews()
    }

    private fun setupViews() {
        mGLSurfaceView = GLSurfaceView(this)
        setContentView(mGLSurfaceView)
        //设置版本
        mGLSurfaceView!!.setEGLContextClientVersion(3)
        val renderer = bindRenderer()
        mGLSurfaceView!!.setRenderer(renderer)
    }

}