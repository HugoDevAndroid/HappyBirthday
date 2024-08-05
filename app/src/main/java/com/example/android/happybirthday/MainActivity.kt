package com.example.android.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android.happybirthday.ui.HappyBirthday
import com.example.android.happybirthday.ui.theme.HappyBIrthdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBIrthdayTheme {
                HappyBirthday(
                    saludo = R.string.saludo,
                    indicacion = R.string.instruccion,
                    textoBoton = R.string.txt_boton
                )
            }
        }
    }
}
