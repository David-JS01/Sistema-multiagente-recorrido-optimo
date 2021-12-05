package es.usal.pa.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Linea implements Serializable {
	
	private List<Float> inicioLinea;
    private List<Integer> parada;
    private List<Float> tiempos;
    private String nombre;
    private int tipo;
    
	public Linea(List<Float> inicioLinea, List<Integer> parada, List<Float> tiempos, String nombre, int tipo) {
		this.inicioLinea = inicioLinea;
		this.parada = parada;
		this.tiempos = tiempos;
		this.nombre = nombre;
		this.tipo = tipo;
	}
	
	public Linea () {
		this.inicioLinea= new ArrayList<>();
		this.parada= new ArrayList<>();
		this.tiempos= new ArrayList<>();
	}

	public List<Float> getInicioLinea() {
		return inicioLinea;
	}

	public void setInicioLinea(List<Float> inicioLinea) {
		this.inicioLinea = inicioLinea;
	}

	public List<Integer> getParada() {
		return parada;
	}

	public void setParada(List<Integer> parada) {
		this.parada = parada;
	}

	public List<Float> getTiempos() {
		return tiempos;
	}

	public void setTiempos(List<Float> tiempos) {
		this.tiempos = tiempos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	
	
	
    
    

}
