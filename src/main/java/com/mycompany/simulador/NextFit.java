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

public class NextFit extends AdministradorDeMemoria {
    private int posicionActual;

    public NextFit(int tamanoMemoria, RegistroDeEventos nombreArchivoRegistro) {
        super(tamanoMemoria);
        this.posicionActual = 0; // Inicializa la posición actual al inicio
    }


    @Override
    public void asignarMemoria(Trabajo trabajo) {
        int tamanoTrabajo = trabajo.getMemoriaRequerida();
        List<BloqueDeMemoria> bloques = obtenerBloquesDeMemoria();
        int numBloques = bloques.size();

        // Iterar sobre los bloques, empezando desde la última posición de asignación
        for (int i = 0; i < numBloques; i++) {
            // Calcular el índice actual considerando que se reinicia si se llega al final de la lista
            int indiceActual = (posicionActual + i) % numBloques;
            BloqueDeMemoria bloque = bloques.get(indiceActual);

            if (bloque.estaLibre() && bloque.getTamano() >= tamanoTrabajo) {
                System.out.println("Bloque disponible encontrado para el trabajo: " + trabajo.getNombreProceso());
                trabajo.setDireccionInicio(bloque.getDireccionInicio());

                // Marcar el bloque actual como ocupado y ajustar su tamaño
                int tamanoBloque = bloque.getTamano();
                bloque.ocupar();
                bloque.setTamano(tamanoTrabajo); // Ajustar el bloque al tamaño requerido por el trabajo

                // Calcular si hay memoria restante en el bloque
                int tamanoRestante = tamanoBloque - tamanoTrabajo;

                // Si hay memoria restante, crear un nuevo bloque con el espacio sobrante
                if (tamanoRestante > 0) {
                    System.out.println("Creando un nuevo bloque libre con tamaño restante: " + tamanoRestante);

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

                // Actualizar la posición de la última asignación
                posicionActual = indiceActual;

                return;
            }
        }

        // Si no se encontró un bloque adecuado
        System.out.println("No se encontró bloque disponible para el trabajo: " + trabajo.getNombreProceso());
        registrarEvento("No se pudo asignar memoria al trabajo: " + trabajo.getNombreProceso());
    }

    @Override
    public void liberarMemoria(Trabajo trabajo) {
        List<BloqueDeMemoria> bloques = obtenerBloquesDeMemoria(); // Obtener la lista de bloques
        System.out.println("Intentando liberar memoria para el trabajo: " + trabajo.getNombreProceso() +
                           " con dirección de inicio: " + trabajo.getDireccionInicio());

        // Buscar el bloque correspondiente al trabajo
        for (BloqueDeMemoria bloque : bloques) {
            System.out.println("Revisando bloque en direccion: " + bloque.getDireccionInicio() + 
                               " con tamaño: " + bloque.getTamano() + 
                               " (Libre: " + bloque.estaLibre() + ")");

            if (bloque.getDireccionInicio() == trabajo.getDireccionInicio() && !bloque.estaLibre()) {
                System.out.println("Se encontró el bloque correspondiente. Liberando...");
                bloque.liberar(); // Liberar el bloque

                // Registrar el evento de liberación de memoria
                registrarEvento("Se ha liberado memoria para el trabajo: " + trabajo.getNombreProceso());

                // Fusionar particiones libres adyacentes
                fusionarParticionesLibres();

                // Actualizar la posición de última asignación si corresponde
                posicionActual = bloques.indexOf(bloque);
                System.out.println("Actualizando la posición de la última asignación a: " + posicionActual);

                return;
            }
        }

        // Si no se encontró el bloque correspondiente, registrar un evento
        System.out.println("No se encontró bloque para liberar memoria para el trabajo: " + trabajo.getNombreProceso());
        registrarEvento("No se encontró bloque para liberar memoria para el trabajo: " + trabajo.getNombreProceso());
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

        int tamanoTrabajo = trabajo.getMemoriaRequerida();
        int numBloques = bloquesDeMemoria.size();

        // Iterar sobre los bloques, empezando desde la última posición de asignación
        for (int i = 0; i < numBloques; i++) {
            int indiceActual = (posicionActual + i) % numBloques;
            BloqueDeMemoria bloque = bloquesDeMemoria.get(indiceActual);

            System.out.println("Chequeando bloque de memoria en dirección: " + bloque.getDireccionInicio() +
                               " - Tamaño: " + bloque.getTamano() + 
                               " - Estado libre: " + bloque.estaLibre());

            if (bloque.estaLibre() && bloque.getTamano() >= tamanoTrabajo) {
                System.out.println("Bloque disponible encontrado para trabajo: " + trabajo.getNombreProceso());
                return true;
            }
        }

        System.out.println("No se encontró bloque disponible para el trabajo: " + trabajo.getNombreProceso());
        return false;
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



