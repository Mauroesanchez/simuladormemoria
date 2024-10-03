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

public class WorstFit extends AdministradorDeMemoria {

    public WorstFit(int tamanoMemoria, RegistroDeEventos nombreArchivoRegistro) {
        super(tamanoMemoria);
    }


    @Override
    public void asignarMemoria(Trabajo trabajo) {
        BloqueDeMemoria peorBloque = null;

        // Buscar el peor bloque que se ajuste al trabajo
        for (BloqueDeMemoria bloque : obtenerBloquesDeMemoria()) {
            if (bloque.estaLibre() && bloque.getTamano() >= trabajo.getMemoriaRequerida()) {
                if (peorBloque == null || bloque.getTamano() > peorBloque.getTamano()) {
                    peorBloque = bloque;
                }
            }
        }

        if (peorBloque != null) {
            // Asignar memoria al trabajo
            peorBloque.ocupar();
            // Actualizar el bloque si es necesario
            int tamanoRestante = peorBloque.getTamano() - trabajo.getMemoriaRequerida();
            if (tamanoRestante > 0) {
                // Crear un nuevo bloque libre con el tamaño restante
                BloqueDeMemoria nuevoBloque = new BloqueDeMemoria(peorBloque.getDireccionInicio() + trabajo.getMemoriaRequerida(), tamanoRestante, true);
                agregarBloque(nuevoBloque);
            }
            // Registro de eventos
            registrarEvento("Se ha asignado memoria al trabajo: " + trabajo.getNombreProceso() + " en el peor bloque encontrado.");
        } else {
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

    // Buscar la posición donde el nuevo bloque debe ser insertado, contiguo al bloque ocupado
    for (int i = 0; i < bloques.size(); i++) {
        BloqueDeMemoria bloqueActual = bloques.get(i);

        // Verificar si el bloque actual termina justo antes de donde comienza el nuevo bloque
        if (bloqueActual.getDireccionInicio() + bloqueActual.getTamano() == nuevoBloque.getDireccionInicio()) {
            // Insertar el nuevo bloque libre en la lista justo después del bloque ocupado
            bloques.add(i + 1, nuevoBloque);
            return;
        }
    }

    // Si no se encuentra una posición adecuada, agregar el nuevo bloque al final de la lista
    bloques.add(nuevoBloque);
}

}


