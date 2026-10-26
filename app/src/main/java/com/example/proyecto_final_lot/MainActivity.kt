package com.example.proyecto_final_lot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final_lot.ui.theme.Proyecto_final_lotTheme
import com.example.proyecto_final_lot.vistas.LoginScreen
import com.example.proyecto_final_lot.vistas.MonitoreoScreen
import com.example.proyecto_final_lot.vistas.RecuperarScreen
import com.example.proyecto_final_lot.vistas.RegistroScreen
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

// Traducion de los errores técnicos de Firebase //
private fun mensajeError(e: Exception?): String = when (e) {
    is FirebaseAuthInvalidUserException,
    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"
    is FirebaseAuthUserCollisionException -> "Ese correo ya está registrado"
    is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil"
    is FirebaseTooManyRequestsException -> "Demasiados intentos. Espera unos minutos"
    is FirebaseNetworkException -> "Sin conexión a internet"
    else -> "Ocurrió un error. Intenta de nuevo"
}

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        auth = FirebaseAuth.getInstance()

        enableEdgeToEdge()
        setContent {
            Proyecto_final_lotTheme {
                val navController = rememberNavController()

                var cargando by rememberSaveable { mutableStateOf(false) }
                var errorMensaje by rememberSaveable { mutableStateOf<String?>(null) }
                var exitoMensaje by rememberSaveable { mutableStateOf<String?>(null) }

                // monitoreo datos fijos por ahora
                var humedadActual by rememberSaveable { mutableStateOf(45) }
                var temperaturaActual by rememberSaveable { mutableStateOf(24) }
                var estadoDeConexion by rememberSaveable { mutableStateOf("Conectado") }

                fun limpiarMensajes() {
                    errorMensaje = null
                    exitoMensaje = null
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            // Si ya hay sesión guardada, entra directo al monitoreo
                            startDestination = if (auth.currentUser != null) "monitoreo" else "login"
                        ) {
                            // 1. LOGIN
                            composable("login") {
                                LoginScreen(
                                    cargando = cargando,
                                    error = errorMensaje,
                                    onIniciarSesion = { correo, clave ->
                                        cargando = true
                                        limpiarMensajes()
                                        auth.signInWithEmailAndPassword(correo, clave)
                                            .addOnCompleteListener { task ->
                                                cargando = false
                                                if (task.isSuccessful) {
                                                    navController.navigate("monitoreo") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                } else {
                                                    errorMensaje = mensajeError(task.exception)
                                                }
                                            }
                                    },
                                    onIrARegistro = {
                                        limpiarMensajes()
                                        navController.navigate("register")
                                    },
                                    onIrARecuperar = {
                                        limpiarMensajes()
                                        navController.navigate("recuperar")
                                    }
                                )
                            }

                            // 2. REGISTRO
                            composable("register") {
                                RegistroScreen(
                                    cargando = cargando,
                                    error = errorMensaje,
                                    onRegistrar = { correo, clave ->
                                        cargando = true
                                        limpiarMensajes()
                                        auth.createUserWithEmailAndPassword(correo, clave)
                                            .addOnCompleteListener { task ->
                                                cargando = false
                                                if (task.isSuccessful) {
                                                    navController.navigate("monitoreo") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                } else {
                                                    errorMensaje = mensajeError(task.exception)
                                                }
                                            }
                                    },
                                    onVolverALogin = {
                                        limpiarMensajes()
                                        navController.popBackStack()
                                    }
                                )
                            }

                            // 3. RECUPERAR CONTRASEÑA
                            composable("recuperar") {
                                RecuperarScreen(
                                    cargando = cargando,
                                    error = errorMensaje,
                                    mensajeExito = exitoMensaje,
                                    onEnviarCorreo = { correo ->
                                        cargando = true
                                        limpiarMensajes()
                                        auth.sendPasswordResetEmail(correo)
                                            .addOnCompleteListener { task ->
                                                cargando = false
                                                if (task.isSuccessful) {
                                                    exitoMensaje = "Si el correo está registrado, recibirás un enlace para crear una contraseña nueva."
                                                } else {
                                                    errorMensaje = mensajeError(task.exception)
                                                }
                                            }
                                    },
                                    onVolverALogin = {
                                        limpiarMensajes()
                                        navController.popBackStack()
                                    }
                                )
                            }

                            // 4. MONITOREO (IoT)
                            composable("monitoreo") {
                                MonitoreoScreen(
                                    humedad = humedadActual,
                                    temperatura = temperaturaActual,
                                    estadoConexion = estadoDeConexion,
                                    onConectar = {
                                        // TODO: aquí va la conexión Bluetooth real con el HC-05.
                                        // Cuando esté lista, actualiza estadoDeConexion,
                                        // humedadActual y temperaturaActual con lo que llegue.
                                        estadoDeConexion = "Conectando..."
                                    },
                                    onAbrir = {
                                        // TODO: enviar 'A' por Bluetooth
                                    },
                                    onCerrar = {
                                        // TODO: enviar 'C' por Bluetooth
                                    },
                                    onSilenciar = {
                                        // TODO: enviar 'S' por Bluetooth
                                    },
                                    onCerrarSesion = {
                                        auth.signOut()
                                        estadoDeConexion = "Desconectado"
                                        humedadActual = 0
                                        temperaturaActual = 0
                                        limpiarMensajes()
                                        navController.navigate("login") {
                                            popUpTo("monitoreo") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}