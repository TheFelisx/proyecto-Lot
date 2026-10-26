package com.example.proyecto_final_lot.vistas


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RecuperarScreen(
    onEnviarCorreo: (correo: String) -> Unit,
    onVolverALogin: () -> Unit,
    cargando: Boolean = false,
    mensajeExito: String? = null,
    error: String? = null
) {
    var correo by remember { mutableStateOf("") }
    var aviso by remember { mutableStateOf<String?>(null) }

    val colores = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF007ACC),
        unfocusedBorderColor = Color(0xFF55606E),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color(0xFFA0AAB5),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White
    )

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A222D)).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "RECUPERAR CONTRASEÑA", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(
            "Te enviaremos un enlace a tu correo para crear una contraseña nueva.",
            fontSize = 13.sp, color = Color(0xFFA0AAB5),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            label = { Text("Correo electrónico") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = colores,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        val mensajeError = aviso ?: error
        if (mensajeError != null) {
            Text(mensajeError, color = Color(0xFFEF5350), fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
        }
        if (mensajeExito != null) {
            Text(mensajeExito, color = Color(0xFF66BB6A), fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
        }

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = !cargando,
            onClick = {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
                    aviso = "Ingresa un correo válido"
                } else {
                    aviso = null
                    onEnviarCorreo(correo.trim())
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007ACC)),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(if (cargando) "Enviando..." else "Enviar enlace", color = Color.White, fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onVolverALogin, modifier = Modifier.padding(top = 16.dp)) {
            Text("Volver al inicio de sesión", color = Color.White)
        }
    }
}