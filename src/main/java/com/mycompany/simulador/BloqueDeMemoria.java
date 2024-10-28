/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simulador;

/**
 *
 * @author Mauro
 */
public class BloqueDeMemoria {
    private int direccionInicio;
    private int tamano;
    private boolean libre;

    public BloqueDeMemoria(int direccionInicio, int tamano, boolean libre) {
        this.direccionInicio = direccionInicio;
        this.tamano = tamano;
        this.libre = libre;
    }

    public int getDireccionInicio() {
        return direccionInicio;
    }

    public void setTamano(int nuevoTamano) {
        this.tamano = nuevoTamano;
    }

    public int getTamano() {
        return tamano;
    }

    public boolean estaLibre() {
        return libre;
    }

    public void ocupar() {
        this.libre = false; // Cambia el estado a ocupado
    }

    public void liberar() {
        this.libre = true; // Cambia el estado a libre
    }

    @Override
    public String toString() {
        return "BloqueDeMemoria{" +
                "direccionInicio=" + direccionInicio +
                ", tamano=" + tamano +
                ", libre=" + libre +
                '}';
    }
}



