package com.example.metrogrid.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.MetroGridTheme

@Composable
fun InternetDialog(){
    Box(
        contentAlignment = Alignment.CenterStart,modifier = Modifier.fillMaxWidth()
        .height(48.dp)
        .background(MaterialTheme.colorScheme.onErrorContainer) )
        {
        Text("Unstable Internet Connection", style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onError,
            ), modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
@Preview
fun InternetDialogPreview(){
    MetroGridTheme {
        InternetDialog()
    }
}