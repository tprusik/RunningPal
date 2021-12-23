package com.example.runningpal.others

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants.KEY_USER_AVATAR
import com.example.runningpal.others.Constants.KEY_USER_BCK
import com.example.runningpal.others.Constants.KEY_USER_EMAIL
import com.example.runningpal.others.Constants.KEY_USER_ID
import com.example.runningpal.others.Constants.KEY_USER_NAME
import com.example.runningpal.others.Constants.KEY_USER_WEIGHT
import com.example.runningpal.others.Constants.SHARED_PREFERENCES_NAME
import timber.log.Timber
import java.io.ByteArrayOutputStream

object Utils {

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

    fun getUserSharedPrevs(context: Context) : User {

        val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val name = sharedPreferences.getString(KEY_USER_NAME,"BLAD")
        val email = sharedPreferences.getString(KEY_USER_EMAIL,null)
        val weight = sharedPreferences.getString(KEY_USER_WEIGHT,null)
        val id = sharedPreferences.getString(KEY_USER_ID,"0")
        val avatar = sharedPreferences.getString(KEY_USER_AVATAR,null)
        val background = sharedPreferences.getString(KEY_USER_BCK,null)


        val user = User().also {
            it.nick = name
            it.uid = id
            it.profilePic = avatar
            it.backgroundPic = background }

        return user
    }

    fun removeSharedPrefs(context: Context) {

        val sharedPreferences: SharedPreferences.Editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        sharedPreferences.clear()
        sharedPreferences.commit()
    }

}