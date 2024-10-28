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

public class WorstFit extends AdministradorDeMemoria {

    public WorstFit(int tamanoMemoria, RegistroDeEventos nombreArchivoRegistro) {
        super(tamanoMemoria);
    }


    @Override
    public void asignarMemoria(Trabajo trabajo) {
        BloqueDeMemoria bloqueWorstFit = null; // Almacenar el bloque más grande disponible

        // Buscar el bloque de mayor tamaño que pueda acomodar el trabajo
        for (BloqueDeMemoria bloque : obtenerBloquesDeMemoria()) {
            if (bloque.estaLibre() && bloque.getTamano() >= trabajo.getMemoriaRequerida()) {
                if (bloqueWorstFit == null || bloque.getTamano() > bloqueWorstFit.getTamano()) {
                    bloqueWorstFit = bloque;
                }
            }
        }

        // Si se encontró un bloque adecuado
        if (bloqueWorstFit != null) {
            System.out.println("Bloque más grande encontrado para el trabajo: " + trabajo.getNombreProceso());
            trabajo.setDireccionInicio(bloqueWorstFit.getDireccionInicio());

            // Asignar memoria al trabajo
            int tamanoTrabajo = trabajo.getMemoriaRequerida();
            int tamanoBloque = bloqueWorstFit.getTamano();

            // Marcar el bloque actual como ocupado y ajustar su tamaño
            bloqueWorstFit.ocupar();
            bloqueWorstFit.setTamano(tamanoTrabajo); // Ajustar el bloque al tamaño requerido por el trabajo

            // Calcular si hay memoria restante en el bloque
            int tamanoRestante = tamanoBloque - tamanoTrabajo;

            // Si hay memoria restante, crear un nuevo bloque con el espacio sobrante
            if (tamanoRestante > 0) {
                System.out.println("Creando un nuevo bloque libre con tamaño restante: " + tamanoRestante);

                // Crear el nuevo bloque libre con la memoria restante
                BloqueDeMemoria nuevoBloque = new BloqueDeMemoria(
                    bloqueWorstFit.getDireccionInicio() + tamanoTrabajo, // Nueva dirección de inicio
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
        List<BloqueDeMemoria> bloques = obtenerBloquesDeMemoria(); // Obtener la lista de bloques
        System.out.println("Intentando liberar memoria para el trabajo: " + trabajo.getNombreProceso() +
                           " con dirección de inicio: " + trabajo.getDireccionInicio());

        // Buscar el bloque correspondiente al trabajo
        for (BloqueDeMemoria bloque : bloques) {
            System.out.println("Revisando bloque en direccion: " + bloque.getDireccionInicio() + 
                               " con tamanio: " + bloque.getTamano() + 
                               " (Libre: " + bloque.estaLibre() + ")");

            // Si el bloque coincide con la dirección de inicio del trabajo y está ocupado
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

    
    @Override
    public boolean hayMemoriaDisponible(Trabajo trabajo) {
        System.out.println("Obteniendo bloques de memoria para trabajo: " + trabajo.getNombreProceso());

        // Obtener todos los bloques de memoria antes de hacer el chequeo
        List<BloqueDeMemoria> bloquesDeMemoria = obtenerBloquesDeMemoria(); 
        System.out.println("Cantidad de bloques obtenidos: " + bloquesDeMemoria.size());

        BloqueDeMemoria peorBloque = null; // Variable para almacenar el bloque más grande

        // Iterar sobre los bloques de memoria para encontrar el peor (el más grande que se pueda usar)
        for (BloqueDeMemoria bloque : bloquesDeMemoria) {
            System.out.println("Chequeando bloque de memoria en direccion: " + bloque.getDireccionInicio() + 
                               " - Tamanio: " + bloque.getTamano() + 
                               " - Estado libre: " + bloque.estaLibre());

            // Si el bloque está libre y puede acomodar el trabajo
            if (bloque.estaLibre() && bloque.getTamano() >= trabajo.getMemoriaRequerida()) {
                // Si es el primer bloque encontrado o es más grande que el bloque actual
                if (peorBloque == null || bloque.getTamano() > peorBloque.getTamano()) {
                    peorBloque = bloque;
                }
            }
        }

        // Verificar si se encontró un bloque que cumpla con los requisitos
        if (peorBloque != null) {
            System.out.println("Peor bloque disponible encontrado para el trabajo: " + trabajo.getNombreProceso() +
                               " en direccion: " + peorBloque.getDireccionInicio() +
                               " con tamanio: " + peorBloque.getTamano());
            return true;
        }

        System.out.println("No se encontro bloque disponible suficientemente grande para el trabajo: " + trabajo.getNombreProceso());
        return false;
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


