/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

import java.util.ArrayList;
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
                System.out.println("Bloque disponible encontrado para el trabajo: " + trabajo.getNombreProceso());
                trabajo.setDireccionInicio(bloque.getDireccionInicio());
                // Asignar memoria al trabajo
                int tamanoTrabajo = trabajo.getMemoriaRequerida();
                int tamanoBloque = bloque.getTamano();

                // Marcar el bloque actual como ocupado y ajustar su tamaño
                bloque.ocupar();
                bloque.setTamano(tamanoTrabajo); // Ajustar el bloque al tamaño requerido por el trabajo

                // Calcular si hay memoria restante en el bloque
                int tamanoRestante = tamanoBloque - tamanoTrabajo;

                // Si hay memoria restante, crear un nuevo bloque con el espacio sobrante
                if (tamanoRestante > 0) {
                    System.out.println("Creando un nuevo bloque libre con tamanio restante: " + tamanoRestante);

                    // Crear el nuevo bloque libre con la memoria restante
                    BloqueDeMemoria nuevoBloque = new BloqueDeMemoria(
                        bloque.getDireccionInicio() + tamanoTrabajo, // Nueva dirección de inicio
                        tamanoRestante, // Tamaño del bloque restante
                        true // Estado: libre
                    );

                    // Agregar el nuevo bloque a la lista de bloques
                    agregarBloque(nuevoBloque);
                }

                // Registrar el evento de asignación de memoria
                registrarEvento("Se ha asignado memoria al trabajo: " + trabajo.getNombreProceso());
                return;
            }
        }

        // Si no se encontró un bloque adecuado
        System.out.println("No se encontro bloque disponible para el trabajo: " + trabajo.getNombreProceso());
        registrarEvento("No se pudo asignar memoria al trabajo: " + trabajo.getNombreProceso());
    }


    
    @Override
    public boolean hayMemoriaDisponible(Trabajo trabajo) {
        System.out.println("Obteniendo bloques de memoria para trabajo: " + trabajo.getNombreProceso());

        // Obtener todos los bloques de memoria antes de hacer el chequeo
        List<BloqueDeMemoria> bloquesDeMemoria = obtenerBloquesDeMemoria(); 
        System.out.println("Cantidad de bloques obtenidos: " + bloquesDeMemoria.size());

        // Iterar sobre los bloques de memoria para verificar si hay alguno disponible
        for (BloqueDeMemoria bloque : bloquesDeMemoria) {
            System.out.println("Chequeando bloque de memoria en direccion: " + bloque.getDireccionInicio() + 
                               " - Tamanio: " + bloque.getTamano() + 
                               " - Estado libre: " + bloque.estaLibre());

            if (bloque.estaLibre() && bloque.getTamano() >= trabajo.getMemoriaRequerida()) {
                System.out.println("Bloque disponible encontrado para trabajo: " + trabajo.getNombreProceso());
                return true;
            }
        }

        System.out.println("No se encontro bloque disponible para trabajo: " + trabajo.getNombreProceso());
        return false;
    }



    @Override
    public void liberarMemoria(Trabajo trabajo) {
        List<BloqueDeMemoria> bloques = obtenerBloquesDeMemoria(); // Obtener la lista de bloques
        System.out.println("Intentando liberar memoria para el trabajo: " + trabajo.getNombreProceso() +
                           " con dirección de inicio: " + trabajo.getDireccionInicio());

        // Buscar el bloque correspondiente al trabajo
        for (BloqueDeMemoria bloque : bloques) {
            System.out.println("Revisando bloque en direccion: " + bloque.getDireccionInicio() + 
                               " con tamanio: " + bloque.getTamano() + 
                               " (Libre: " + bloque.estaLibre() + ")");

            if (bloque.getDireccionInicio() == trabajo.getDireccionInicio() && !bloque.estaLibre()) {
                System.out.println("Se encontro el bloque correspondiente. Liberando...");
                bloque.liberar(); // Liberar el bloque

                // Registrar el evento de liberación de memoria
                registrarEvento("Se ha liberado memoria para el trabajo: " + trabajo.getNombreProceso());

                // Fusionar particiones libres adyacentes
                fusionarParticionesLibres();
                return;
            }
        }

        // Si no se encontró el bloque correspondiente, registrar un evento
        System.out.println("No se encontro bloque para liberar memoria para el trabajo: " + trabajo.getNombreProceso());
        registrarEvento("No se encontro bloque para liberar memoria para el trabajo: " + trabajo.getNombreProceso());
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


