package com.example.oink.ui.startAplication

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.R
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.ui.theme.robotoSemiBoldStyle
import com.example.oink.utils.LocaleHelper


@Composable
fun StartUpScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {}
) {
    val context = LocalContext.current


    val logo = rememberImageAsset(context, "logo.png")
    val translateIcon = rememberImageAsset(context, "g_translate.png")
    val centralImage = rememberImageAsset(context, "CentralImage.png")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {


                translateIcon?.let {
                    Image(
                        bitmap = it,
                        contentDescription = stringResource(R.string.desc_translate),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { 
                                LocaleHelper.changeLanguage(context)
                                Toast.makeText(context, context.getString(R.string.btn_change_language), Toast.LENGTH_SHORT).show()
                            }
                    )
                }
            }

            logo?.let {
                Image(
                    bitmap = it,
                    contentDescription = stringResource(R.string.desc_logo),
                    modifier = Modifier
                        .width(120.dp)
                        .height(70.dp)
                        .offset(y = (-70).dp)
                )
            }


            centralImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = stringResource(R.string.desc_main_image),
                    modifier = Modifier
                        .width(297.dp)
                        .height(233.dp)
                        .offset(y = (-70).dp)
                )
            }


            Text(
                text = stringResource(R.string.slogan),
                style = robotoSemiBoldStyle(
                    fontSize = 32.sp,
                    color = Color(0xFF0D3685)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = (-120).dp)
            )


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onRegisterClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2997FD),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(308.dp)
                        .height(56.dp)
                        .offset(y=(-30).dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_register_me),
                        style = robotoBoldStyle(
                            fontSize = 20.sp,
                            color = Color(0xFFFFFFFF)
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                OutlinedButton(
                    onClick = { onLoginClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White

                    ),
                    border = BorderStroke(2.dp, Color(0xFF2997FD)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(308.dp)
                        .height(56.dp)
                        .offset(y=(-30).dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_have_account),
                        style = robotoBoldStyle(
                            fontSize = 20.sp,
                            color = Color(0xFF2997FD)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberImageAsset(context: android.content.Context, fileName: String) =
    remember(fileName) {
        runCatching {
            val inputStream = context.assets.open(fileName)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }.getOrNull()
    }

@Preview(showBackground = true)
@Composable
fun PreviewStartScreen() {
    StartUpScreen()
}
