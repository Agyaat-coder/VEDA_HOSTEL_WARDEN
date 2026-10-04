package com.devesh.vedahostelwarden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.devesh.vedahostelwarden.navigation.AppNavHost
import com.devesh.vedahostelwarden.ui.theme.VEDAHOSTELWARDENTheme
import com.devesh.vedahostelwarden.ui.theme.VedaNavyBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VEDAHOSTELWARDENTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VedaNavyBackground
                ) {
                    AppNavHost()
                }
            }
        }
    }
}
