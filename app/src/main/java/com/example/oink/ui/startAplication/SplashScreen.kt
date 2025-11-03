package com.example.oink.ui.startAplication

import android.graphics.BitmapFactory
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
        delay(2500)
        onTimeout()
    }


    val bitmap = remember {
        val inputStream = context.assets.open("CentralImage.png")
        BitmapFactory.decodeStream(inputStream)
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
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Splash logo",
                modifier = Modifier
                    .width(297.dp)
                    .height(233.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tu mejor compañía financiera",
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
