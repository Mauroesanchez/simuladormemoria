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
    }

    // Método para cargar trabajos en la cola (puedes modificar los valores según sea necesario)
    void cargarTrabajos() {
        System.out.println("Cargando trabajos...");
        //Carga de procesos en tipo (Arribo, duracion, memoria)
        trabajos.add(new Trabajo("Proceso1", 0, 10, 50));
        trabajos.add(new Trabajo("Proceso2", 1, 5, 30));
        trabajos.add(new Trabajo("Proceso3", 2, 8, 20));
        trabajos.add(new Trabajo("Proceso4", 3, 12, 40));
        trabajos.add(new Trabajo("Proceso5", 5, 6, 50));
        trabajos.add(new Trabajo("Proceso6", 6, 10, 60));
        trabajos.add(new Trabajo("Proceso7", 7, 12, 80));
        // Puedes agregar más trabajos según sea necesario
        
        
        //Aqui dejo 3 tandas mas Comentadas para pruebas, descomentar o borrar en caso de pruebas
        // trabajos.add(new Trabajo("Proceso1", 0, 15, 45));
        // trabajos.add(new Trabajo("Proceso2", 1, 7, 25));
        // trabajos.add(new Trabajo("Proceso3", 3, 10, 30));
        // trabajos.add(new Trabajo("Proceso4", 4, 5, 50));
        // trabajos.add(new Trabajo("Proceso5", 6, 8, 35));
        // trabajos.add(new Trabajo("Proceso6", 8, 12, 20));
        // trabajos.add(new Trabajo("Proceso7", 10, 9, 60));
        
        
        // trabajos.add(new Trabajo("Proceso1", 0, 6, 40));
        // trabajos.add(new Trabajo("Proceso2", 2, 9, 25));
        // trabajos.add(new Trabajo("Proceso3", 4, 7, 60));
        // trabajos.add(new Trabajo("Proceso4", 5, 12, 30));
        // trabajos.add(new Trabajo("Proceso5", 7, 8, 20));
        // trabajos.add(new Trabajo("Proceso6", 9, 10, 55));
        // trabajos.add(new Trabajo("Proceso7", 11, 6, 50));
        
        
        // trabajos.add(new Trabajo("Proceso1", 0, 10, 35));
        // trabajos.add(new Trabajo("Proceso2", 1, 5, 45));
        // trabajos.add(new Trabajo("Proceso3", 3, 15, 25));
        // trabajos.add(new Trabajo("Proceso4", 5, 6, 55));
        // trabajos.add(new Trabajo("Proceso5", 6, 12, 40));
        // trabajos.add(new Trabajo("Proceso6", 9, 8, 30));
        // trabajos.add(new Trabajo("Proceso7", 12, 9, 50));

    }
    
    public Trabajo obtenerTrabajoEnArribo(int tiempoGlobal) {
        for (Trabajo trabajo : trabajos) {  // 'trabajos' es la lista o cola de trabajos
            if (trabajo.getInstanteArribo() == tiempoGlobal) {
                return trabajo;  // Devuelve el trabajo que coincide con el tiempo de arribo
            }
        }
        return null;  // Si no hay trabajos que coincidan, devuelve null
    }

    // Método para obtener la lista de trabajos
    public List<Trabajo> obtenerTrabajos() {
        return trabajos;
    }
    public int obtenerTamanio() {
        return trabajos.size(); // Devuelve el tamaño de la lista de trabajos
    }
    public boolean estaVacia() {
        return trabajos.isEmpty(); // Devuelve true si la lista de trabajos está vacía
    }
    public void removerTrabajo(Trabajo trabajo) {
    for (int i = 0; i < trabajos.size(); i++) {
        if (trabajos.get(i).equals(trabajo)) {
            trabajos.remove(i);
            break;
        }
    }
}

}








