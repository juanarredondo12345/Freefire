package com.example.aimbot

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object AdbInjector {

    fun executeCommand(ip: String, port: Int, command: String) {
        Thread {
            try {
                val socket = Socket(ip, port)
                val writer = PrintWriter(socket.getOutputStream(), true)
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                
                // Enviar comando via ADB shell
                writer.println(command)
                writer.flush()
                
                // Leer respuesta
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    // Procesar salida si es necesario
                }
                
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
