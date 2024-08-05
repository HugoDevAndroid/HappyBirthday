package com.example.android.happybirthday

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
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

@Composable
fun HappyBirthday(
    modifier: Modifier = Modifier,
    @StringRes saludo: Int,
    @StringRes indicacion: Int,
    @StringRes textoBoton: Int,
) {
    var input by remember { mutableStateOf("") }
    var mostrarGif by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val audio = MediaPlayer.create(context, R.raw.hbirthday)

    DisposableEffect(Unit) {
        onDispose {
            audio.release()
        }
    }

    PantallaInicial(
        saludo = context.getString(saludo),
        input = input,
        changeInput = { input = it },
        indicacion = context.getString(indicacion),
        textoBoton = context.getString(textoBoton),
        mostrarGif = { mostrarGif = true }
    )
    if (mostrarGif) {
        ImageGif(
            modifier = modifier.fillMaxSize(),
            context = context,
            nombre = input,
            description = "Background",
            gif = R.drawable.happy_birthday
        )
        Controles(audio = audio)
    }
}

@Composable
fun PantallaInicial(
    modifier: Modifier = Modifier,
    input: String,
    changeInput: (String) -> Unit,
    saludo: String,
    indicacion: String,
    textoBoton: String,
    mostrarGif: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = saludo,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = indicacion,
            fontSize = 24.sp
        )
        Spacer(modifier = modifier.height(24.dp))
        TextField(
            value = input,
            onValueChange = { changeInput(it) },
            textStyle = LocalTextStyle.current.copy(fontSize = 24.sp, textAlign = TextAlign.Center),
            singleLine = true
        )
        Spacer(modifier = modifier.height(24.dp))
        Button(
            onClick = mostrarGif
        ) {
            Text(
                text = textoBoton,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun ImageGif(
    modifier: Modifier,
    context: Context,
    nombre: String,
    description: String,
    @DrawableRes gif: Int,
) {
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(data = gif)
            .apply(block = { size(Size.ORIGINAL) })
            .build(),
        imageLoader = imageLoader
    )

    Image(
        painter = painter,
        contentDescription = description,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 84.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = nombre,
            modifier = Modifier
                .background(
                    Color(0xBB023047),
                    shape = RoundedCornerShape(15.dp)
                )
                .border(
                    width = 5.dp,
                    color = Color(0xFFFFB703),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(20.dp),
            color = Color(0xFFFFB703),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge.copy(
                shadow = Shadow(
                    Color.Gray,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
        )
    }
}

@Composable
fun Controles(
    modifier: Modifier = Modifier,
    audio: MediaPlayer,
) {
    var contador = 0

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(36.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row {
            IconButton(
                onClick = { audio.start() },
                modifier = Modifier.size(45.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(
                onClick = { audio.pause() },
                modifier = Modifier.size(45.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pause),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        audio.setOnCompletionListener {
            if (contador < 2) {
                audio.start()
                contador++
            }
        }
        audio.start()
    }
}

@Preview
@Composable
fun PreviewScreens() {
    HappyBIrthdayTheme {
//        PantallaInicial(
//            saludo = LocalContext.current.getString(R.string.saludo),
//            input = "",
//            changeInput = {},
//            indicacion = LocalContext.current.getString(R.string.instruccion),
//            textoBoton = LocalContext.current.getString(R.string.txt_boton),
//            mostrarGif = {}
//        )
//        ImageGif(
//            modifier = Modifier.fillMaxSize(),
//            context = LocalContext.current,
//            nombre = "Hugo",
//            description = "",
//            gif = R.drawable.happy_birthday
//        )
    }
}
