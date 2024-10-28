/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.simulador;

import java.util.Objects;

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
    private int direccionInicio;         // Dirección de inicio en memoria

    // Constructor
    public Trabajo(String nombreProceso, int instanteArribo, int duracionTrabajo, int memoriaRequerida) {
        this.nombreProceso = nombreProceso;
        this.instanteArribo = instanteArribo;
        this.duracionTrabajo = duracionTrabajo;
        this.memoriaRequerida = memoriaRequerida;
        this.direccionInicio = -1; // Inicializar con un valor que indica que aún no se ha asignado
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
    
    // Método para obtener la dirección de inicio
    public int getDireccionInicio() {
        return direccionInicio;
    }

    // Método para establecer la dirección de inicio
    public void setDireccionInicio(int direccionInicio) {
        this.direccionInicio = direccionInicio;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Trabajo trabajo = (Trabajo) obj;
        // Aquí puedes comparar atributos únicos como el nombre del proceso
        return nombreProceso.equals(trabajo.nombreProceso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreProceso); // Debes utilizar el mismo atributo que comparas en equals()
    }

}



