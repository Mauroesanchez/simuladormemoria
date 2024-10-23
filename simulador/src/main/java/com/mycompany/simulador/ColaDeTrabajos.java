/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

/**
 *
 * @author Mauro
 */
import java.util.ArrayList;
import java.util.List;

public class ColaDeTrabajos {
    private List<Trabajo> trabajos;

    // Constructor
    public ColaDeTrabajos() {
        trabajos = new ArrayList<>();
        cargarTrabajos(); // Cargar trabajos por defecto al iniciar
    }

    // Método para cargar trabajos en la cola (puedes modificar los valores según sea necesario)
    void cargarTrabajos() {
        trabajos.add(new Trabajo("Proceso1", 0, 10, 50));
        trabajos.add(new Trabajo("Proceso2", 1, 5, 30));
        trabajos.add(new Trabajo("Proceso3", 2, 8, 20));
        trabajos.add(new Trabajo("Proceso4", 3, 12, 40));
        // Puedes agregar más trabajos según sea necesario
    }

    // Método para obtener la lista de trabajos
    public List<Trabajo> obtenerTrabajos() {
        return trabajos;
    }

    
}








