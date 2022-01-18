package com.example.runningpal.others

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.util.Util
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UtilsTests {


    private var context = mock(Context::class.java)

    @Before
    fun setup() {

    }

    @Test
    fun getProperFormattedTimeStringIfMillisNotInclude() {
        Utils.getUserSharedPrevs(context)
    }
}