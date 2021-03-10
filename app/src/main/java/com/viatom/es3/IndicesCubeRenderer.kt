package com.viatom.es3

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.viatom.es3.ResReadUtils.readResource
import com.viatom.es3.ShaderUtils.compileFragmentShader
import com.viatom.es3.ShaderUtils.compileVertexShader
import com.viatom.es3.ShaderUtils.linkProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @anchor: andy
 * @date: 2018-11-09
 * @description: 基于索引法绘制立方体
 */
class IndicesCubeRenderer : GLSurfaceView.Renderer {
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private val indicesBuffer: ShortBuffer
    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf( //正面矩形
        0.25f, 0.25f, 0.0f,  //V0
        -0.75f, 0.25f, 0.0f,  //V1
        -0.75f, -0.75f, 0.0f,  //V2
        0.25f, -0.75f, 0.0f,  //V3
        //背面矩形
        0.75f, -0.25f, 0.0f,  //V4
        0.75f, 0.75f, 0.0f,  //V5
        -0.25f, 0.75f, 0.0f,  //V6
        -0.25f, -0.25f, 0.0f //V7
    )

    /**
     * 定义索引
     */
    private val indices = shortArrayOf( //背面
        5, 6, 7, 5, 7, 4,  //左侧
        6, 1, 2, 6, 2, 7,  //底部
        4, 7, 2, 4, 2, 3,  //顶面
        5, 6, 7, 5, 7, 4,  //右侧
        5, 0, 3, 5, 3, 4,  //正面
        0, 1, 2, 0, 2, 3
    )

    //立方体的顶点颜色
    private val colors = floatArrayOf(
        0.3f, 0.4f, 0.5f, 1f,  //V0
        0.3f, 0.4f, 0.5f, 1f,  //V1
        0.3f, 0.4f, 0.5f, 1f,  //V2
        0.3f, 0.4f, 0.5f, 1f,  //V3
        0.6f, 0.5f, 0.4f, 1f,  //V4
        0.6f, 0.5f, 0.4f, 1f,  //V5
        0.6f, 0.5f, 0.4f, 1f,  //V6
        0.6f, 0.5f, 0.4f, 1f //V7
    )

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
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
            .asFloatBuffer()
        //传入指定的坐标数据
        vertexBuffer.put(vertexPoints)
        vertexBuffer.position(0)

        //分配内存空间,每个浮点型占4字节空间
        colorBuffer = ByteBuffer.allocateDirect(colors.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        //传入指定的数据
        colorBuffer.put(colors)
        colorBuffer.position(0)

        //分配内存空间,每个浮点型占4字节空间
        indicesBuffer = ByteBuffer.allocateDirect(indices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
        //传入指定的数据
        indicesBuffer.put(indices)
        indicesBuffer.position(0)
    }
}