/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.simulador;

/**
 *
 * @author Mauro
 */
public class Trabajo {
    private String nombreProceso;       // Nombre del proceso
    private int instanteArribo;         // Instante de arribo
    private int duracionTrabajo;        // Duración total del trabajo
    private int memoriaRequerida;       // Cantidad de memoria requerida
    private int tiempoDeInicio;         // Tiempo en el que inicia el trabajo
    private int tiempoDeFinalizacion;   // Tiempo en el que finaliza el trabajo

    // Constructor
    public Trabajo(String nombreProceso, int instanteArribo, int duracionTrabajo, int memoriaRequerida) {
        this.nombreProceso = nombreProceso;
        this.instanteArribo = instanteArribo;
        this.duracionTrabajo = duracionTrabajo;
        this.memoriaRequerida = memoriaRequerida;
    }

    // Métodos getter y setter
    public String getNombreProceso() {
        return nombreProceso;
    }

    public int getInstanteArribo() {
        return instanteArribo;
    }

    public int getDuracionTrabajo() {
        return duracionTrabajo;
    }

    public int getMemoriaRequerida() {
        return memoriaRequerida;
    }

    public int getTiempoDeInicio() {
        return tiempoDeInicio;
    }

    public void setTiempoDeInicio(int tiempoDeInicio) {
        this.tiempoDeInicio = tiempoDeInicio;
    }

    public int getTiempoDeFinalizacion() {
        return tiempoDeFinalizacion;
    }

    public void setTiempoDeFinalizacion(int tiempoDeFinalizacion) {
        this.tiempoDeFinalizacion = tiempoDeFinalizacion;
    }
}


