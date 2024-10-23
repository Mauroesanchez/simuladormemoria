/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;


/**
 *
 * @author Mauro
 */

import java.util.List;

public class BestFit extends AdministradorDeMemoria {

    public BestFit(int tamanoMemoria, RegistroDeEventos nombreArchivoRegistro) {
        super(tamanoMemoria);
    }


    @Override
    public void asignarMemoria(Trabajo trabajo) {
        BloqueDeMemoria mejorBloque = null;

        // Buscar el mejor bloque que se ajuste al trabajo
        for (BloqueDeMemoria bloque : obtenerBloquesDeMemoria()) {
            if (bloque.estaLibre() && bloque.getTamano() >= trabajo.getMemoriaRequerida()) {
                if (mejorBloque == null || bloque.getTamano() < mejorBloque.getTamano()) {
                    mejorBloque = bloque;
                }
            }
        }

        if (mejorBloque != null) {
            // Asignar memoria al trabajo
            mejorBloque.ocupar();
            // Actualizar el bloque si es necesario
            int tamanoRestante = mejorBloque.getTamano() - trabajo.getMemoriaRequerida();
            if (tamanoRestante > 0) {
                // Crear un nuevo bloque libre con el tamaño restante
                BloqueDeMemoria nuevoBloque = new BloqueDeMemoria(mejorBloque.getDireccionInicio() + trabajo.getMemoriaRequerida(), tamanoRestante, true);
                agregarBloque(nuevoBloque);
            }
            // Registro de eventos
            registrarEvento("Se ha asignado memoria al trabajo: " + trabajo.getNombreProceso() + " en el mejor bloque encontrado.");
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

    // Buscar la posición del bloque ocupado y agregar el bloque libre restante justo después
    for (int i = 0; i < bloques.size(); i++) {
        BloqueDeMemoria bloqueActual = bloques.get(i);

        // Verifica si el bloque actual termina justo antes de donde comienza el nuevo bloque
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
