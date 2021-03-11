package com.vaca.c60.utils

import android.content.res.Resources.NotFoundException
import com.vaca.c60.MyApplication
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * @anchor: andy
 * @date: 2018-09-13
 * @description:
 */
object ResReadUtils {
    /**
     * 读取资源
     *
     * @param resourceId
     * @return
     */
    @JvmStatic
    fun readResource(resourceId: Int): String {
        val builder = StringBuilder()
        try {
            val inputStream = MyApplication.myresources.openRawResource(resourceId)
            val streamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(streamReader)
            var textLine: String?
            while (bufferedReader.readLine().also { textLine = it } != null) {
                builder.append(textLine)
                builder.append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
        return builder.toString()
    }
}