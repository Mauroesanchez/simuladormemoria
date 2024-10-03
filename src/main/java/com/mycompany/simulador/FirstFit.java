/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

import java.util.List;



/**
 *
 * @author Mauro
 */
public class FirstFit extends AdministradorDeMemoria {

    public FirstFit(int tamanoMemoria, RegistroDeEventos nombreArchivoRegistro) {
        super(tamanoMemoria);
    }


    @Override
    public void asignarMemoria(Trabajo trabajo) {
        for (BloqueDeMemoria bloque : obtenerBloquesDeMemoria()) {
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
                // Registro de eventos
                registrarEvento("Se ha asignado memoria al trabajo: " + trabajo.getNombreProceso());
                return;
            }
        }
        // Si no se encontró un bloque adecuado
        registrarEvento("No se pudo asignar memoria al trabajo: " + trabajo.getNombreProceso());
    }

    private void agregarBloque(BloqueDeMemoria nuevoBloque) {
    // Obtener la lista de bloques existentes
    List<BloqueDeMemoria> bloques = obtenerBloquesDeMemoria();

    // Insertar el nuevo bloque justo después del bloque ocupado
    for (int i = 0; i < bloques.size(); i++) {
        BloqueDeMemoria bloqueActual = bloques.get(i);

        // Buscar la posición donde se debe insertar el nuevo bloque
        if (bloqueActual.getDireccionInicio() + bloqueActual.getTamano() == nuevoBloque.getDireccionInicio()) {
            // Insertar el nuevo bloque en la lista
            bloques.add(i + 1, nuevoBloque);
            return;
        }
    }
}


    @Override
    public void liberarMemoria(Trabajo trabajo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}


