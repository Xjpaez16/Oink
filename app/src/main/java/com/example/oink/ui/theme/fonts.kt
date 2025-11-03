package com.example.oink.ui.theme

import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun appFontStyle(
    fontName: String,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black
): TextStyle {
    val context = LocalContext.current

    val typeface = remember(fontName) {
        try {
            Typeface.createFromAsset(context.assets, "fonts/$fontName")
        } catch (e: Exception) {
            Typeface.DEFAULT
        }
    }

    val customFontFamily = remember(typeface) {
        FontFamily(typeface)
    }

    return TextStyle(
        fontFamily = customFontFamily,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color
    )
}


@Composable
fun robotoLightStyle(
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black
) = appFontStyle("Roboto-Light.ttf", fontSize, FontWeight.Light, color)

@Composable
fun robotoRegularStyle(
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black
) = appFontStyle("Roboto-Regular.ttf", fontSize, FontWeight.Normal, color)

@Composable
fun robotoMediumStyle(
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black
) = appFontStyle("Roboto-Medium.ttf", fontSize, FontWeight.Medium, color)

@Composable
fun robotoSemiBoldStyle(
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black
) = appFontStyle("Roboto-SemiBold.ttf", fontSize, FontWeight.SemiBold, color)

@Composable
fun robotoBoldStyle(
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black
) = appFontStyle("Roboto-Bold.ttf", fontSize, FontWeight.Bold, color)