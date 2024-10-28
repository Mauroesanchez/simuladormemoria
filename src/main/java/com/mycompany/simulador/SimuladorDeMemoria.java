/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author Mauro
 */

public class SimuladorDeMemoria {
    private ColaDeTrabajos colaDeTrabajos;
    private AdministradorDeMemoria administradorDeMemoria;
    private RegistroDeEventos registroEventos;

    public SimuladorDeMemoria() {
        colaDeTrabajos = new ColaDeTrabajos();
        registroEventos = new RegistroDeEventos(); // Inicializar el registro de eventos
    }

public void inicializarSimulacion(int tamanoMemoria, String politica, 
                                   int tiempoSeleccionParticion, 
                                   int tiempoCargaPromedio, 
                                   int tiempoLiberacionParticion) {
    // Seleccionar la política de asignación
    switch (politica.toLowerCase()) {
        case "firstfit":
            administradorDeMemoria = new FirstFit(tamanoMemoria, registroEventos);
            break;
        case "bestfit":
            administradorDeMemoria = new BestFit(tamanoMemoria, registroEventos);
            break;
        case "worstfit":
            administradorDeMemoria = new WorstFit(tamanoMemoria, registroEventos);
            break;
        case "nextfit":
            administradorDeMemoria = new NextFit(tamanoMemoria, registroEventos);
            break;
        default:
            throw new IllegalArgumentException("Política de asignación no válida: " + politica);
    }

    // Almacenar los tiempos de partición
    administradorDeMemoria.setTiempoSeleccionParticion(tiempoSeleccionParticion);
    administradorDeMemoria.setTiempoCargaPromedio(tiempoCargaPromedio);
    administradorDeMemoria.setTiempoLiberacionParticion(tiempoLiberacionParticion);

    // Mensaje de confirmación
    System.out.println("Simulacion inicializada con los siguientes parametros:");
    System.out.println("Tamanio de memoria: " + tamanoMemoria);
    System.out.println("Politica de asignacion: " + politica);
    System.out.println("Tiempo de selección de particion: " + tiempoSeleccionParticion);
    System.out.println("Tiempo de carga promedio: " + tiempoCargaPromedio);
    System.out.println("Tiempo de liberacion de particion: " + tiempoLiberacionParticion);
}

public void ejecutarSimulacion() {
    int tiempoGlobal = 0;
    int totalTiempoRetorno = 0;
    int cantidadTrabajos = colaDeTrabajos.obtenerTrabajos().size();
    int tiempoUltimaAsignacion = 0;  // Iniciamos en 0 para manejar el primer trabajo
    List<Trabajo> trabajosEnEjecucion = new ArrayList<>();  // Trabajos que están en ejecución
    List<Trabajo> colaDeEspera = new ArrayList<>();  // Cola de espera local
    int memoriaFragmentada = 0;  // Variable para almacenar la memoria fragmentada
    int tiempoUltimoTrabajoAsignado = -1; // Para rastrear el tiempo de la última asignación

    while (!colaDeTrabajos.estaVacia() || !trabajosEnEjecucion.isEmpty() || !colaDeEspera.isEmpty()) {
        // Sumar la memoria fragmentada en el tiempo global actual
        if (tiempoGlobal == 0 || tiempoGlobal > tiempoUltimoTrabajoAsignado) {
            // Acumular memoria libre en el tiempo actual
            for (BloqueDeMemoria bloque : administradorDeMemoria.obtenerBloquesDeMemoria()) {
                if (bloque.estaLibre()) {
                    memoriaFragmentada += bloque.getTamano();
                }
            }
        }

        // Intentar asignar trabajos de la cola de espera primero
        List<Trabajo> trabajosParaRemover = new ArrayList<>();
        for (Trabajo trabajoEspera : colaDeEspera) {
            // Verificar si ha pasado el tiempo de carga promedio desde la última asignación
            int tiempoDeInicioEspera = Math.max(tiempoGlobal, tiempoUltimaAsignacion + administradorDeMemoria.getTiempoCargaPromedio() + administradorDeMemoria.getTiempoSeleccionParticion());
            if (tiempoGlobal >= tiempoDeInicioEspera && administradorDeMemoria.hayMemoriaDisponible(trabajoEspera)) {
                // Asignar memoria y mover a ejecución
                administradorDeMemoria.asignarMemoria(trabajoEspera);

                // Asignar el tiempo de inicio y finalización
                trabajoEspera.setTiempoDeInicio(tiempoGlobal);
                trabajoEspera.setTiempoDeFinalizacion(tiempoGlobal + trabajoEspera.getDuracionTrabajo());
                System.out.println("Trabajo " + trabajoEspera.getNombreProceso() + " finalizara en el tiempo: " + trabajoEspera.getTiempoDeFinalizacion());

                trabajosEnEjecucion.add(trabajoEspera);
                trabajosParaRemover.add(trabajoEspera); // Marcar para remover
                System.out.println("Trabajo " + trabajoEspera.getNombreProceso() + " re-asignado desde la cola de espera en tiempo: " + tiempoGlobal);

                // Actualizar el tiempo de última asignación
                tiempoUltimaAsignacion = tiempoGlobal;
                tiempoUltimoTrabajoAsignado = tiempoGlobal; // Actualizamos el tiempo del último trabajo asignado
            }
        }

        // Remover trabajos que fueron asignados desde la cola de espera
        colaDeEspera.removeAll(trabajosParaRemover);

        // Verificar si algún trabajo ha llegado en el tiempo global actual
        Trabajo trabajo = colaDeTrabajos.obtenerTrabajoEnArribo(tiempoGlobal);
        if (trabajo != null) {
            System.out.println("Tiempo global: " + tiempoGlobal + " - Se encontro trabajo: " + trabajo.getNombreProceso());

            // Calcular el tiempo mínimo de inicio: debe respetar el tiempo desde la última asignación
            int tiempoDeInicio = Math.max(trabajo.getInstanteArribo(), tiempoUltimaAsignacion + administradorDeMemoria.getTiempoCargaPromedio() + administradorDeMemoria.getTiempoSeleccionParticion());

            // Verificar si se puede asignar memoria y si ha pasado suficiente tiempo desde la última asignación
            if (tiempoGlobal >= tiempoDeInicio && administradorDeMemoria.hayMemoriaDisponible(trabajo)) {
                // Asignar memoria al trabajo
                administradorDeMemoria.asignarMemoria(trabajo);
                System.out.println("Trabajo " + trabajo.getNombreProceso() + " asignado en el tiempo: " + tiempoGlobal);
                System.out.println("Memoria requerida: " + trabajo.getMemoriaRequerida() + " - Duración: " + trabajo.getDuracionTrabajo());

                // Asignar el tiempo de inicio y calcular el tiempo de finalización
                trabajo.setTiempoDeInicio(tiempoGlobal);
                trabajo.setTiempoDeFinalizacion(tiempoGlobal + trabajo.getDuracionTrabajo());

                // Agregar el trabajo a la lista de trabajos en ejecución
                trabajosEnEjecucion.add(trabajo);

                // Calcular el tiempo total de retorno
                totalTiempoRetorno += trabajo.getTiempoDeFinalizacion() - trabajo.getInstanteArribo();

                // Registrar evento
                registroEventos.registrarEvento("Trabajo " + trabajo.getNombreProceso() + " asignado en " + tiempoGlobal);

                // Calcular el tiempo de liberación de partición
                trabajo.setTiempoDeFinalizacion(trabajo.getTiempoDeFinalizacion() + administradorDeMemoria.getTiempoLiberacionParticion());
                colaDeTrabajos.removerTrabajo(trabajo);

                // Actualizar el tiempo de última asignación
                tiempoUltimaAsignacion = tiempoGlobal; // Actualizamos al tiempo global actual
                tiempoUltimoTrabajoAsignado = tiempoGlobal; // Actualizamos el tiempo del último trabajo asignado
            } else {
                // Si no hay memoria suficiente o no ha pasado el tiempo de espera, enviar a la cola de espera
                System.out.println("No hay memoria suficiente o no ha pasado el tiempo de espera para el trabajo: " + trabajo.getNombreProceso() + ". Enviando a la cola de espera.");
                colaDeEspera.add(trabajo); // Agregar a la cola de espera
                colaDeTrabajos.removerTrabajo(trabajo); // Remover del trabajo original
            }
        }

        // Verificar si algún trabajo ha finalizado en el tiempo global actual
        List<Trabajo> trabajosFinalizados = new ArrayList<>();
        for (Trabajo t : trabajosEnEjecucion) {
            if (tiempoGlobal == t.getTiempoDeFinalizacion()) {
                System.out.println("Trabajo " + t.getNombreProceso() + " finalizó en el tiempo: " + tiempoGlobal);
                // Liberar la memoria utilizada por el trabajo
                administradorDeMemoria.liberarMemoria(t);

                // Registrar evento de liberación
                int retornoProceso = tiempoGlobal - t.getInstanteArribo();
                registroEventos.registrarEvento("Trabajo " + t.getNombreProceso() + " Tiempo de retorno: " + retornoProceso);

                // Agregar el trabajo a la lista de finalizados
                trabajosFinalizados.add(t);

                // Después de liberar memoria, verificar si las particiones adyacentes están libres y fusionarlas
                administradorDeMemoria.fusionarParticionesLibres();
            }
        }

        // Eliminar los trabajos que ya finalizaron de la lista de trabajos en ejecución
        trabajosEnEjecucion.removeAll(trabajosFinalizados);

        // Incrementar el tiempo global
        tiempoGlobal++;
    }

    // Calcular la memoria fragmentada al final de la simulación
    int memoriaFragmentadaFinal = 0;
    for (BloqueDeMemoria bloque : administradorDeMemoria.obtenerBloquesDeMemoria()) {
        if (bloque.estaLibre()) {
            memoriaFragmentadaFinal += bloque.getTamano();
        }
    }

    // Mostrar resultados finales
    totalTiempoRetorno = tiempoGlobal-1;
    double tiempoMedioRetorno = (double) totalTiempoRetorno / cantidadTrabajos;
    System.out.println("Tiempo de retorno de la tanda: " + totalTiempoRetorno);
    System.out.println("Memoria fragmentada total: " + memoriaFragmentada);
    System.out.println("Tiempo Medio de Retorno: " + tiempoMedioRetorno);
}



    
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Ingresar el tamaño de la memoria física disponible
    System.out.println("Ingrese el tamanio de la memoria fisica disponible:");
    int tamanoMemoria = scanner.nextInt();

    scanner.nextLine(); // Limpiar el buffer

    // Ingresar la política de asignación
    System.out.println("Ingrese la politica de asignacion (firstfit, bestfit, worstfit, nextfit):");
    String politica = scanner.nextLine();

    // Ingresar el tiempo de selección de partición
    System.out.println("Ingrese el tiempo de seleccion de particion:");
    int tiempoSeleccionParticion = scanner.nextInt();

    // Ingresar el tiempo de carga promedio
    System.out.println("Ingrese el tiempo de carga promedio:");
    int tiempoCargaPromedio = scanner.nextInt();

    // Ingresar el tiempo de liberación de partición
    System.out.println("Ingrese el tiempo de liberacion de particion:");
    int tiempoLiberacionParticion = scanner.nextInt();

    // Crear el simulador y cargar los trabajos
    SimuladorDeMemoria simulador = new SimuladorDeMemoria();
    simulador.colaDeTrabajos.cargarTrabajos(); // Cargar trabajos directamente en la cola

    // Inicializar la simulación con los parámetros ingresados
    simulador.inicializarSimulacion(tamanoMemoria, politica, tiempoSeleccionParticion, tiempoCargaPromedio, tiempoLiberacionParticion);

    // Ejecutar la simulación
    simulador.ejecutarSimulacion();

    // Cerrar el scanner
    scanner.close();
}
}












