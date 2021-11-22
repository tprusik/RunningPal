package com.example.runningpal.others

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object DatabaseUtility {

    fun convertStringToBitmap(pic : String) : Bitmap{

        val decodedString: ByteArray = Base64.decode(pic, Base64.URL_SAFE)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

    }

    fun convertBitmapToString(bmp : Bitmap) : String{

         val bitmap = bmp!!.copy(bmp.config,true)

            val bao = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao) // bmp is bitmap from user image file
            bitmap.recycle()
            val byteArray: ByteArray = bao.toByteArray()
            val imageB64: String = Base64.encodeToString(byteArray, Base64.URL_SAFE)

            return imageB64

    }


}