/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

/**
 *
 * @author Mauro
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RegistroDeEventos {
    private List<String> eventos;
    private String nombreArchivo;

    public RegistroDeEventos() {
        this.eventos = new ArrayList<>();
        this.nombreArchivo = nombreArchivo;
    }

    

    public void registrarEvento(String evento) {
        String mensaje = System.currentTimeMillis() + ": " + evento;
        //eventos.add(mensaje);
        System.out.println(mensaje); // También imprime en la consola
    }

    public void guardarEventos() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (String evento : eventos) {
                writer.println(evento);
            }
        }
    }

     public void cerrarRegistro() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String evento : eventos) {
                writer.write(evento);
                writer.newLine(); // Agrega una nueva línea después de cada evento
            }
            System.out.println("Registro de eventos guardado en " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar el registro de eventos: " + e.getMessage());
        }
    }
}


