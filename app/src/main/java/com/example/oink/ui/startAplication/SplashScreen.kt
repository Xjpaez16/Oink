package com.example.oink.ui.startAplication

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.example.oink.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.oink.ui.theme.robotoSemiBoldStyle
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit = {}
) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        Log.d("SplashScreen", "LaunchedEffect start - will delay then call onTimeout")
        delay(2500)
        try {
            onTimeout()
            Log.d("SplashScreen", "onTimeout called")
        } catch (e: Exception) {
            Log.e("SplashScreen", "Error calling onTimeout", e)
        }
    }


    val bitmap = remember {
        runCatching {
            val inputStream = context.assets.open("CentralImage.png")
            BitmapFactory.decodeStream(inputStream)
        }.getOrNull()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            bitmap?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Splash logo",
                    modifier = Modifier
                        .width(297.dp)
                        .height(233.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.slogan),
                style = robotoSemiBoldStyle(
                    fontSize = 32.sp,
                    color = Color(0xFF0D3685)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}
