package com.viatom.es3.renderer

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.viatom.es3.R
import com.viatom.es3.utils.ResReadUtils.readResource
import com.viatom.es3.utils.ShaderUtils.compileFragmentShader
import com.viatom.es3.utils.ShaderUtils.compileVertexShader
import com.viatom.es3.utils.ShaderUtils.linkProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class CubeRenderer : GLSurfaceView.Renderer {
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private val indicesBuffer: ShortBuffer

    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf(
        0.5f, 0.0f, 0.0f,
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.0f, 0.0f,
    )

    /**
     * 定义索引
     */
    private val indices = shortArrayOf(
       0,1,2
    )

    //立方体的顶点颜色
    private val colors = floatArrayOf(
       1f, 0.0f, 0.0f, 1f,
        1f, 0.0f, 0.0f, 1f,
        1f, 0.0f, 0.0f, 1f,
        0f, 1f, 0.0f, 1f,
        0f, 0.0f, 1f, 1f,

    )

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        yes()
    }

    fun yes(){
        //设置背景颜色
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        //编译
        val vertexShaderId = compileVertexShader(readResource(R.raw.vertex_colorcube_shader))
        val fragmentShaderId = compileFragmentShader(readResource(R.raw.fragment_colorcube_shader))
        //链接程序片段
        mProgram = linkProgram(vertexShaderId, fragmentShaderId)
        //使用程序片段
        GLES30.glUseProgram(mProgram)
        GLES30.glVertexAttribPointer(
            0,
            VERTEX_POSITION_SIZE,
            GLES30.GL_FLOAT,
            false,
            0,
            vertexBuffer
        )
        //启用位置顶点属性
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(1, VERTEX_COLOR_SIZE, GLES30.GL_FLOAT, false, 0, colorBuffer)
        //启用颜色顶点属性
        GLES30.glEnableVertexAttribArray(1)
    }


    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glDrawElements(
            GL10.GL_TRIANGLES,
            indices.size,
            GL10.GL_UNSIGNED_SHORT,
            indicesBuffer
        )
    }

    companion object {
        private const val VERTEX_POSITION_SIZE = 3
        private const val VERTEX_COLOR_SIZE = 4
    }

    init {
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(vertexPoints)
                position(0)
            }

        //分配内存空间,每个浮点型占4字节空间
        colorBuffer = ByteBuffer.allocateDirect(colors.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(colors)
                position(0)
            }


        indicesBuffer = ByteBuffer.allocateDirect(indices.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer().apply {
                put(indices)
                position(0)
            }
    }
}