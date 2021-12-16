package com.vaca.c60.activity

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Half
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vaca.c60.R

import com.vaca.c60.renderer.CubeRenderer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class MainActivity : AppCompatActivity() {
    private lateinit var mGLSurfaceView: GLSurfaceView
    private lateinit var renderer: CubeRenderer
    private val NS2S = 1.0f / 1000000000.0f
    private val deltaRotationVector = FloatArray(4) { 0f }
    private var timestamp: Float = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(object: SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if(touchDown){
                    return
                }
                // This timestep's delta rotation to be multiplied by the current rotation
                // after computing it from the gyro sample data.
                if (timestamp != 0f && event != null) {
                    val dT = (event.timestamp - timestamp) * NS2S
                    // Axis of the rotation sample, not normalized yet.
                    var axisX: Float = event.values[0]
                    var axisY: Float = event.values[1]
                    var axisZ: Float = event.values[2]
                    renderer.angleX = (axisY)
                    renderer.angleY = (axisX)
                    renderer.angleZ = (axisZ)
                    // Calculate the angular speed of the sample
                    val omegaMagnitude: Float = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ)

                    // Normalize the rotation vector if it's big enough to get the axis
                    // (that is, EPSILON should represent your maximum allowable margin of error)
                    if (omegaMagnitude > Half.EPSILON) {
                        axisX /= omegaMagnitude
                        axisY /= omegaMagnitude
                        axisZ /= omegaMagnitude
                    }

                    // Integrate around this axis with the angular speed by the timestep
                    // in order to get a delta rotation from this sample over the timestep
                    // We will convert this axis-angle representation of the delta rotation
                    // into a quaternion before turning it into the rotation matrix.
                    val thetaOverTwo: Float = omegaMagnitude * dT / 2.0f
                    val sinThetaOverTwo: Float = sin(thetaOverTwo)
                    val cosThetaOverTwo: Float = cos(thetaOverTwo)
                    deltaRotationVector[0] = sinThetaOverTwo * axisX
                    deltaRotationVector[1] = sinThetaOverTwo * axisY
                    deltaRotationVector[2] = sinThetaOverTwo * axisZ
                    deltaRotationVector[3] = cosThetaOverTwo
                }
                timestamp = event?.timestamp?.toFloat() ?: 0f
                val deltaRotationMatrix = FloatArray(9) { 0f }
                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
                // User code should concatenate the delta rotation we computed with the current rotation
                // in order to get the updated rotation.
                // rotationCurrent = rotationCurrent * deltaRotationMatrix;
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        },sensor,100)
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    var touchDown=false



    @SuppressLint("ClickableViewAccessibility")
    val gaga = View.OnTouchListener { _, event ->
        val x = event!!.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                touchDown=true
                Log.e("move","asdf")
                var dx = x - previousX
                var dy = y - previousY
                renderer.angleX = (dx / 5)
                renderer.angleY = (dy / 5)
            }

            MotionEvent.ACTION_UP->{
                touchDown=false
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
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }

}