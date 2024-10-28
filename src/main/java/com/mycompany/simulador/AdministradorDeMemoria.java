/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

import java.util.ArrayList;
import java.util.List;

public abstract class AdministradorDeMemoria {
    protected int tamanoMemoria;
    protected List<BloqueDeMemoria> bloquesDeMemoria;
    private int tiempoSeleccionParticion;
    private int tiempoCargaPromedio;
    private int tiempoLiberacionParticion;

    public AdministradorDeMemoria(int tamanoMemoria) {
        this.tamanoMemoria = tamanoMemoria;
        this.bloquesDeMemoria = new ArrayList<>();
        inicializarBloquesDeMemoria();
    }

    // Inicializa la memoria con un bloque libre
    private void inicializarBloquesDeMemoria() {
        bloquesDeMemoria.add(new BloqueDeMemoria(0, tamanoMemoria, true)); // Bloque libre inicial
    }

    public abstract void asignarMemoria(Trabajo trabajo);

    public List<BloqueDeMemoria> obtenerBloquesDeMemoria() {
        return bloquesDeMemoria;
    }

    // Método para registrar eventos 
    protected void registrarEvento(String evento) {
        // Implementar lógica para registrar el evento en el archivo o en memoria
        System.out.println(evento); // Por ahora, solo imprimimos el evento
    }
    public abstract void fusionarParticionesLibres();

    // Método para liberar memoria (implementado en las subclases)
    public abstract void liberarMemoria(Trabajo trabajo);

    void cerrarRegistro() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setTiempoSeleccionParticion(int tiempo) {
        this.tiempoSeleccionParticion = tiempo;
    }

    public void setTiempoCargaPromedio(int tiempo) {
        this.tiempoCargaPromedio = tiempo;
    }

    public void setTiempoLiberacionParticion(int tiempo) {
        this.tiempoLiberacionParticion = tiempo;
    }
    
    public int getTiempoSeleccionParticion() {
        return tiempoSeleccionParticion;
    }

    public int getTiempoCargaPromedio() {
        return tiempoCargaPromedio;
    }

    public int getTiempoLiberacionParticion() {
        return tiempoLiberacionParticion;
    }
    
    public void actualizarBloques(List<BloqueDeMemoria> bloquesFusionados) {
        this.bloquesDeMemoria = bloquesFusionados; // Asumiendo que tienes un atributo para bloques de memoria
    }

    public abstract boolean hayMemoriaDisponible(Trabajo trabajo);
}



