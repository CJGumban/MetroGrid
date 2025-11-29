package com.example.metrogrid.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.compose.MetroGridTheme

@Composable
fun ErrorDialog(
    message: String,
    buttonText: String,
    onDismiss: ()-> Unit = {},
){
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {}
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally ,
            modifier = Modifier
                .shadow(2.dp)
                .padding(16.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ).padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Text(text = message,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color =  MaterialTheme.colorScheme.onSurface

                ),
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            )

            TextButton (onClick = {onDismiss()},

                colors = ButtonDefaults.textButtonColors().copy(containerColor = MaterialTheme.colorScheme.onError),
                shape = RoundedCornerShape(24.dp)){
                    Text(buttonText, color =MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 16.dp) )
                }


            }
        }
    }


@Composable
@Preview
fun ErrorDialogPreview(
){
    MetroGridTheme {
        ErrorDialog(
            "No Internet Connetion",
            "Dismiss"
        )
    }
}