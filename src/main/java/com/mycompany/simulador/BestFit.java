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

public class BestFit extends AdministradorDeMemoria {

    public BestFit(int tamanoMemoria, RegistroDeEventos nombreArchivoRegistro) {
        super(tamanoMemoria);
    }


    @Override
    public void asignarMemoria(Trabajo trabajo) {
        BloqueDeMemoria mejorBloque = null;  // Para almacenar el bloque que mejor se ajuste
        int tamanoMemoriaRequerida = trabajo.getMemoriaRequerida();

        // Buscar el bloque que mejor se ajuste
        for (BloqueDeMemoria bloque : obtenerBloquesDeMemoria()) {
            if (bloque.estaLibre() && bloque.getTamano() >= tamanoMemoriaRequerida) {
                if (mejorBloque == null || bloque.getTamano() < mejorBloque.getTamano()) {
                    mejorBloque = bloque;  // Actualizar si este bloque es más pequeño
                }
            }
        }

        // Si se encontró el mejor bloque
        if (mejorBloque != null) {
            System.out.println("Mejor bloque encontrado para el trabajo: " + trabajo.getNombreProceso());
            trabajo.setDireccionInicio(mejorBloque.getDireccionInicio());

            // Asignar memoria al trabajo
            int tamanoBloque = mejorBloque.getTamano();

            // Marcar el bloque actual como ocupado y ajustar su tamaño
            mejorBloque.ocupar();
            mejorBloque.setTamano(tamanoMemoriaRequerida);  // Ajustar el bloque al tamaño requerido por el trabajo

            // Calcular si hay memoria restante en el bloque
            int tamanoRestante = tamanoBloque - tamanoMemoriaRequerida;

            // Si hay memoria restante, crear un nuevo bloque con el espacio sobrante
            if (tamanoRestante > 0) {
                System.out.println("Creando un nuevo bloque libre con tamanio restante: " + tamanoRestante);

                // Crear el nuevo bloque libre con la memoria restante
                BloqueDeMemoria nuevoBloque = new BloqueDeMemoria(
                    mejorBloque.getDireccionInicio() + tamanoMemoriaRequerida, // Nueva dirección de inicio
                    tamanoRestante, // Tamaño del bloque restante
                    true // Estado: libre
                );

                // Agregar el nuevo bloque a la lista de bloques
                agregarBloque(nuevoBloque);
            }

            // Registrar el evento de asignación de memoria
            registrarEvento("Se ha asignado memoria al trabajo: " + trabajo.getNombreProceso());
        } else {
            // Si no se encontró un bloque adecuado
            System.out.println("No se encontró bloque disponible para el trabajo: " + trabajo.getNombreProceso());
            registrarEvento("No se pudo asignar memoria al trabajo: " + trabajo.getNombreProceso());
        }
    }


    @Override
    public void liberarMemoria(Trabajo trabajo) {
        System.out.println("Intentando liberar memoria para el trabajo: " + trabajo.getNombreProceso() + 
                           " con dirección de inicio: " + trabajo.getDireccionInicio());

        // Iterar sobre los bloques de memoria para encontrar el bloque asignado al trabajo
        for (BloqueDeMemoria bloque : obtenerBloquesDeMemoria()) {
            System.out.println("Revisando bloque en dirección: " + bloque.getDireccionInicio() + 
                               " con tamaño: " + bloque.getTamano() + " (Libre: " + bloque.estaLibre() + ")");

            // Verificar si este bloque coincide con la dirección de inicio del trabajo y no está libre
            if (!bloque.estaLibre() && bloque.getDireccionInicio() == trabajo.getDireccionInicio()) {
                // Marcar el bloque como libre
                bloque.liberar();
                System.out.println("Memoria liberada para el trabajo: " + trabajo.getNombreProceso());

                // Registrar el evento de liberación de memoria
                registrarEvento("Se ha liberado la memoria del trabajo: " + trabajo.getNombreProceso());

                // Fusionar particiones adyacentes si están libres
                fusionarParticionesLibres();
                return;
            }
        }

        System.out.println("No se encontró bloque para liberar memoria para el trabajo: " + trabajo.getNombreProceso());
        registrarEvento("No se pudo liberar memoria para el trabajo: " + trabajo.getNombreProceso());
    }


    
    @Override
    public boolean hayMemoriaDisponible(Trabajo trabajo) {
        System.out.println("Obteniendo bloques de memoria para trabajo: " + trabajo.getNombreProceso());

        // Obtener todos los bloques de memoria antes de hacer el chequeo
        List<BloqueDeMemoria> bloquesDeMemoria = obtenerBloquesDeMemoria(); 
        System.out.println("Cantidad de bloques obtenidos: " + bloquesDeMemoria.size());

        // Inicializar la variable para almacenar el mejor bloque
        BloqueDeMemoria mejorBloque = null;

        // Iterar sobre los bloques de memoria para verificar si hay alguno disponible
        for (BloqueDeMemoria bloque : bloquesDeMemoria) {
            System.out.println("Chequeando bloque de memoria en direccion: " + bloque.getDireccionInicio() + 
                               " - Tamanio: " + bloque.getTamano() + 
                               " - Estado libre: " + bloque.estaLibre());

            // Verificar si el bloque está libre y tiene el tamaño suficiente
            if (bloque.estaLibre() && bloque.getTamano() >= trabajo.getMemoriaRequerida()) {
                // Si es el primer bloque encontrado o es mejor que el bloque actual, actualizar mejorBloque
                if (mejorBloque == null || bloque.getTamano() < mejorBloque.getTamano()) {
                    mejorBloque = bloque;
                }
            }
        }

        // Si se encontró un bloque que cumple con los requisitos
        if (mejorBloque != null) {
            System.out.println("Mejor bloque disponible encontrado para trabajo: " + trabajo.getNombreProceso() +
                               " en direccion: " + mejorBloque.getDireccionInicio() +
                               " con tamanio: " + mejorBloque.getTamano());
            return true;
        }

        System.out.println("No se encontro bloque disponible para trabajo: " + trabajo.getNombreProceso());
        return false;
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
    
    @Override
    public void fusionarParticionesLibres() {
        List<BloqueDeMemoria> bloques = obtenerBloquesDeMemoria(); // Obtener la lista de bloques
        List<BloqueDeMemoria> bloquesFusionados = new ArrayList<>(); // Lista para almacenar bloques fusionados

        for (int i = 0; i < bloques.size(); i++) {
            BloqueDeMemoria bloqueActual = bloques.get(i);

            // Si el bloque actual es libre, buscar bloques adyacentes libres
            if (bloqueActual.estaLibre()) {
                int direccionInicioFusion = bloqueActual.getDireccionInicio();
                int tamanoFusion = bloqueActual.getTamano();

                // Comenzar a fusionar bloques libres adyacentes
                while (i + 1 < bloques.size() && bloques.get(i + 1).estaLibre()) {
                    BloqueDeMemoria siguienteBloque = bloques.get(i + 1);
                    tamanoFusion += siguienteBloque.getTamano(); // Aumentar el tamaño de la fusión
                    i++; // Avanzar al siguiente bloque
                }

                // Crear un nuevo bloque fusionado
                BloqueDeMemoria bloqueFusionado = new BloqueDeMemoria(direccionInicioFusion, tamanoFusion, true);
                bloquesFusionados.add(bloqueFusionado);
            } else {
                // Si el bloque no es libre, agregarlo tal cual a la lista fusionada
                bloquesFusionados.add(bloqueActual);
            }
        }

        // Actualizar la lista de bloques con los bloques fusionados
        actualizarBloques(bloquesFusionados);
    }

}
