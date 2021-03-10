package com.viatom.es3.renderer

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
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
import kotlin.random.Random


class CubeRenderer : GLSurfaceView.Renderer {
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private val indicesBuffer: ShortBuffer

    private var mProgram = 0

    /**
     * 点的坐标
     */
    private var vertexPoints = floatArrayOf(
        0f,1.26295146067f,1.192569588f,
        0f,1.63147573033f,0.596284793999f,
        1.1342010778f,1.26295146067f,0.368524269667f,
        0.567100538899f,1.63147573033f,0.184262134833f,
        0.70097481616f,1.26295146067f,-0.964809063667f,
        0.35048740808f,1.63147573033f,-0.482404531833f,
        -0.70097481616f,1.26295146067f,-0.964809063667f,
        -0.35048740808f,1.63147573033f,-0.482404531833f,
        -1.1342010778f,1.26295146067f,0.368524269667f,
        -0.567100538899f,1.63147573033f,0.184262134833f,
        1.1342010778f,0.894427191f,0.964809063666f,
        0.567100538899f,0.894427191f,1.37683172283f,
        -1.1342010778f,0.894427191f,0.964809063666f,
        -0.567100538899f,0.894427191f,1.37683172283f,
        -0.70097481616f,-0.298142397001f,1.56109385767f,
        -0.35048740808f,0.298142397001f,1.67497411983f,
        0.70097481616f,-0.298142397001f,1.56109385767f,
        0.35048740808f,0.298142397001f,1.67497411983f,
        1.26807535506f,0.894427191f,-0.780546928834f,
        1.48468848588f,0.894427191f,-0.113880262166f,
        1.26807535506f,-0.298142397001f,1.1490711985f,
        1.48468848588f,0.298142397001f,0.8509288015f,
        1.7013016167f,-0.298142397001f,-0.184262134834f,
        1.7013016167f,0.298142397001f,0.184262134834f,
        -0.350487408081f,0.894427191f,-1.4472135955f,
        0.350487408081f,0.894427191f,-1.4472135955f,
        0.35048740808f,-0.298142397001f,-1.67497411983f,
        0.70097481616f,0.298142397001f,-1.56109385767f,
        1.48468848588f,-0.298142397001f,-0.8509288015f,
        1.26807535506f,0.298142397001f,-1.1490711985f,
        -1.48468848588f,0.894427191f,-0.113880262166f,
        -1.26807535506f,0.894427191f,-0.780546928834f,
        -0.35048740808f,-0.298142397001f,-1.67497411983f,
        -0.70097481616f,0.298142397001f,-1.56109385767f,
        -1.48468848588f,-0.298142397001f,-0.8509288015f,
        -1.26807535506f,0.298142397001f,-1.1490711985f,
        -1.7013016167f,-0.298142397001f,-0.184262134834f,
        -1.7013016167f,0.298142397001f,0.184262134834f,
        -1.26807535506f,-0.298142397001f,1.1490711985f,
        -1.48468848588f,0.298142397001f,0.8509288015f,
        0f,-1.26295146067f,-1.192569588f,
        0f,-1.63147573033f,-0.596284793999f,
        -1.1342010778f,-1.26295146067f,-0.368524269667f,
        -0.567100538899f,-1.63147573033f,-0.184262134833f,
        -0.70097481616f,-1.26295146067f,0.964809063667f,
        -0.35048740808f,-1.63147573033f,0.482404531833f,
        0.70097481616f,-1.26295146067f,0.964809063667f,
        0.35048740808f,-1.63147573033f,0.482404531833f,
        1.1342010778f,-1.26295146067f,-0.368524269667f,
        0.567100538899f,-1.63147573033f,-0.184262134833f,
        -1.1342010778f,-0.894427191f,-0.964809063666f,
        -0.567100538899f,-0.894427191f,-1.37683172283f,
        1.1342010778f,-0.894427191f,-0.964809063666f,
        0.567100538899f,-0.894427191f,-1.37683172283f,
        -1.26807535506f,-0.894427191f,0.780546928834f,
        -1.48468848588f,-0.894427191f,0.113880262166f,
        0.350487408081f,-0.894427191f,1.4472135955f,
        -0.350487408081f,-0.894427191f,1.4472135955f,
        1.48468848588f,-0.894427191f,0.113880262166f,
        1.26807535506f,-0.894427191f,0.780546928834f

    )

    /**
     * 定义索引
     */
    private var indices =ShortArray(20*4*3+12*3*3)

    //立方体的顶点颜色
    private var colors = FloatArray(60*4)

    private val six= intArrayOf(0,1,9,8,12,13,1,0,11,10,2,3,3,2,19,18,4,5,5,4,25,24,6,7,7,6,31,30,8,9,10,11,17,16,20,21,13,12,39,38,14,15,15,14,57,56,16,17,18,19,23,22,28,29,21,20,59,58,22,23,24,25,27,26,32,33,26,27,29,28,52,53,30,31,35,34,36,37,33,32,51,50,34,35,37,36,55,54,38,39,40,41,43,42,50,51,41,40,53,52,48,49,42,43,45,44,54,55,44,45,47,46,56,57,46,47,49,48,58,59)

    val five= intArrayOf(11,0,13,15,17,1,3,5,7,9,2,10,21,23,19,4,18,29,27,25,6,24,33,35,31,12,8,30,37,39,14,38,54,44,57,20,16,56,46,59,28,22,58,48,52,32,26,53,40,51,36,34,50,42,55,43,41,49,47,45)


    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
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
        GLES30.glVertexAttribPointer(1, VERTEX_COLOR_SIZE, GLES30.GL_FLOAT, true, 0, colorBuffer)
        //启用颜色顶点属性
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glClearDepthf(1.0f); // 设置深度缓存
        GLES30.glEnable( GLES30.GL_DEPTH_TEST);    // 启用深度测试
        GLES30.glDepthFunc( GLES30.GL_LEQUAL);     // 深度测试类型
    }




    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        Log.e("fuck","fuck $width    $height")
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT)
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
        for(k in vertexPoints.indices){
            vertexPoints[k]=vertexPoints[k]/5f
        }

        for(k in 0 until 60){
            colors[k*4]= Random.nextFloat()
            colors[k*4+1]=Random.nextFloat()
            colors[k*4+2]=Random.nextFloat()
            colors[k*4+3]=1f
        }

        var x=0
        for(k in 0 until 20){
            for(j in 0 until 4){
                indices[x]=six[6*k].toShort()
                x++
                indices[x]=six[6*k+1+j].toShort()
                x++
                indices[x]=six[6*k+2+j].toShort()
                x++
            }
        }

        for(k in 0 until 12){
            for(j in 0 until 3){
                indices[x]=five[5*k].toShort()
                x++
                indices[x]=five[5*k+1+j].toShort()
                x++
                indices[x]=five[5*k+2+j].toShort()
                x++
            }
        }

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