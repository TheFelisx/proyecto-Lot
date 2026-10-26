package com.example.proyecto_final_lot.vistas


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MonitoreoScreen(
    humedad: Int,
    temperatura: Int,
    estadoConexion: String,
    onConectar: () -> Unit,
    onAbrir: () -> Unit,
    onCerrar: () -> Unit,
    onSilenciar: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val conectado = estadoConexion == "Conectado"
    val colorSenal = when (estadoConexion) {
        "Conectado" -> Color(0xFF2E7D32)
        "Conectando..." -> Color(0xFFF57C00)
        else -> Color(0xFFC62828)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Barra superior con el estado de la conexión
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Monitoreo de Humedad - INACAP", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF333333), modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(10.dp).background(colorSenal, CircleShape))
                Spacer(Modifier.width(6.dp))
                Text(estadoConexion, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colorSenal)
            }
        }

        Button(
            onClick = onConectar,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007ACC)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(if (conectado) "Cambiar dispositivo" else "Conectar a la maqueta", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))

        // PARÁMETROS
        TarjetaParametro("Humedad Relativa de la Pieza", "$humedad %", Color(0xFF1976D2))
        TarjetaParametro("Temperatura Ambiente", "$temperatura °C", Color(0xFFE64A19))

        Text(
            "Comandos de Anulación y Alerta", fontSize = 14.sp, fontWeight = FontWeight.Bold,
            color = Color.Gray, modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )

        Row(Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Button(
                enabled = conectado, onClick = onAbrir,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier.weight(1f).padding(end = 8.dp).height(50.dp)
            ) { Text("ABRIR", fontWeight = FontWeight.Bold, color = Color.White) }

            Button(
                enabled = conectado, onClick = onCerrar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                modifier = Modifier.weight(1f).padding(start = 8.dp).height(50.dp)
            ) { Text("CERRAR", fontWeight = FontWeight.Bold, color = Color.White) }
        }

        Button(
            enabled = conectado, onClick = onSilenciar,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575)),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("SILENCIAR ALARMA", fontWeight = FontWeight.Bold, color = Color.White) }

        TextButton(
            onClick = onCerrarSesion,
            modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
        ) { Text("Cerrar sesión", color = Color(0xFF555555)) }
    }
}

@Composable
private fun TarjetaParametro(titulo: String, valor: String, color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, color = Color.Gray, fontSize = 14.sp)
            Text(valor, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}