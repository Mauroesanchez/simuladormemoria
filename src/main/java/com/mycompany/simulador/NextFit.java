/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

import java.io.IOException;

/**
 *
 * @author Mauro
 */
import java.util.List;

public class NextFit extends AdministradorDeMemoria {
    private int posicionActual;

    public NextFit(int tamanoMemoria, RegistroDeEventos nombreArchivoRegistro) {
        super(tamanoMemoria);
        this.posicionActual = 0; // Inicializa la posición actual al inicio
    }


    @Override
    public void asignarMemoria(Trabajo trabajo) {
        int n = obtenerBloquesDeMemoria().size();
        boolean encontrado = false;

        // Buscar el bloque libre desde la última posición conocida
        for (int i = 0; i < n; i++) {
            // Calcular la posición a revisar
            int indice = (posicionActual + i) % n;
            BloqueDeMemoria bloque = obtenerBloquesDeMemoria().get(indice);
            
            if (bloque.estaLibre() && bloque.getTamano() >= trabajo.getMemoriaRequerida()) {
                // Asignar memoria al trabajo
                bloque.ocupar();
                
                // Actualizar el bloque si es necesario
                int tamanoRestante = bloque.getTamano() - trabajo.getMemoriaRequerida();
                if (tamanoRestante > 0) {
                    // Crear un nuevo bloque libre con el tamaño restante
                    BloqueDeMemoria nuevoBloque = new BloqueDeMemoria(bloque.getDireccionInicio() + trabajo.getMemoriaRequerida(), tamanoRestante, true);
                    agregarBloque(nuevoBloque);
                }
                
                // Registrar evento
                registrarEvento("Se ha asignado memoria al trabajo: " + trabajo.getNombreProceso() + " en el bloque encontrado en Next Fit.");
                posicionActual = indice; // Actualiza la posición actual
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            // Si no se encontró un bloque adecuado
            registrarEvento("No se pudo asignar memoria al trabajo: " + trabajo.getNombreProceso());
        }
    }

    @Override
    public void liberarMemoria(Trabajo trabajo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void agregarBloque(BloqueDeMemoria nuevoBloque) {
    List<BloqueDeMemoria> bloques = obtenerBloquesDeMemoria();
    
    // Buscar la posición del bloque ocupado y agregar el bloque libre restante justo después
    for (int i = 0; i < bloques.size(); i++) {
        BloqueDeMemoria bloqueActual = bloques.get(i);

        // Si el bloque actual coincide con el bloque antes del espacio restante
        if (bloqueActual.getDireccionInicio() + bloqueActual.getTamano() == nuevoBloque.getDireccionInicio()) {
            // Insertar el nuevo bloque en la lista justo después del bloque ocupado
            bloques.add(i + 1, nuevoBloque);
            return;
        }
    }

    // Si no se encuentra una posición específica, agregar el bloque al final
    bloques.add(nuevoBloque);
}

}



