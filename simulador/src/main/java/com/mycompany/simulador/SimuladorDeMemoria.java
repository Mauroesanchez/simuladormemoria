/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Mauro
 */
import java.util.Scanner;

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
        System.out.println("Simulación inicializada con los siguientes parámetros:");
        System.out.println("Tamaño de memoria: " + tamanoMemoria);
        System.out.println("Política de asignación: " + politica);
        System.out.println("Tiempo de selección de partición: " + tiempoSeleccionParticion);
        System.out.println("Tiempo de carga promedio: " + tiempoCargaPromedio);
        System.out.println("Tiempo de liberación de partición: " + tiempoLiberacionParticion);
    }

    public void ejecutarSimulacion() {
    int totalTiempoRetorno = 0;
    int cantidadTrabajos = colaDeTrabajos.obtenerTrabajos().size();

    for (Trabajo trabajo : colaDeTrabajos.obtenerTrabajos()) {
        // Calcular el tiempo de inicio considerando el tiempo de carga promedio
        int tiempoDeInicio = trabajo.getInstanteArribo() + administradorDeMemoria.getTiempoCargaPromedio();

        // Calcular el tiempo total de selección de partición
        tiempoDeInicio += administradorDeMemoria.getTiempoSeleccionParticion();

        // Asignar memoria al trabajo
        administradorDeMemoria.asignarMemoria(trabajo);
        
        // Calcular el tiempo de finalización
        trabajo.setTiempoDeInicio(tiempoDeInicio);
        trabajo.setTiempoDeFinalizacion(tiempoDeInicio + trabajo.getDuracionTrabajo());
        
        // Calcular el tiempo total de retorno
        totalTiempoRetorno += trabajo.getTiempoDeFinalizacion() - trabajo.getInstanteArribo();
        
        // Registrar evento
        registroEventos.registrarEvento("Trabajo " + trabajo.getNombreProceso() + " asignado en " + tiempoDeInicio);

        // Calcular el tiempo de liberación de partición
        // Este tiempo se puede agregar al tiempo de finalización
        trabajo.setTiempoDeFinalizacion(trabajo.getTiempoDeFinalizacion() + administradorDeMemoria.getTiempoLiberacionParticion());
    }

    // Cálculo de métricas finales
    double tiempoMedioDeRetorno = (double) totalTiempoRetorno / cantidadTrabajos;
    System.out.println("Tiempo Medio de Retorno: " + tiempoMedioDeRetorno);

    // Cerrar el registro de eventos
    registroEventos.cerrarRegistro();
}

    
    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Ingresar el tamaño de la memoria física disponible
    System.out.println("Ingrese el tamaño de la memoria física disponible:");
    int tamanoMemoria = scanner.nextInt();

    scanner.nextLine(); // Limpiar el buffer

    // Ingresar la política de asignación
    System.out.println("Ingrese la política de asignación (firstfit, bestfit, worstfit, nextfit):");
    String politica = scanner.nextLine();

    // Ingresar el tiempo de selección de partición
    System.out.println("Ingrese el tiempo de selección de partición (en milisegundos):");
    int tiempoSeleccionParticion = scanner.nextInt();

    // Ingresar el tiempo de carga promedio
    System.out.println("Ingrese el tiempo de carga promedio (en milisegundos):");
    int tiempoCargaPromedio = scanner.nextInt();

    // Ingresar el tiempo de liberación de partición
    System.out.println("Ingrese el tiempo de liberación de partición (en milisegundos):");
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












