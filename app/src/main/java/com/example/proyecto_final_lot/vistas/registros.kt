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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistroScreen(
    onRegistrar: (correo: String, clave: String) -> Unit,
    onVolverALogin: () -> Unit,
    cargando: Boolean = false,
    error: String? = null
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var repetir by remember { mutableStateOf("") }
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
            "CREAR CUENTA", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            label = { Text("Correo electrónico") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = colores,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = clave, onValueChange = { clave = it },
            label = { Text("Contraseña (mín. 6 caracteres)") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = colores,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = repetir, onValueChange = { repetir = it },
            label = { Text("Repetir contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = colores,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        val mensaje = aviso ?: error
        if (mensaje != null) {
            Text(mensaje, color = Color(0xFFEF5350), fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
        }

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = !cargando,
            onClick = {
                aviso = when {
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> "Correo no válido"
                    clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                    clave != repetir -> "Las contraseñas no coinciden"
                    else -> null
                }
                if (aviso == null) onRegistrar(correo.trim(), clave)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(if (cargando) "Registrando..." else "Registrarme", color = Color.White, fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onVolverALogin, modifier = Modifier.padding(top = 16.dp)) {
            Text("Ya tengo cuenta, volver", color = Color.White)
        }
    }
}