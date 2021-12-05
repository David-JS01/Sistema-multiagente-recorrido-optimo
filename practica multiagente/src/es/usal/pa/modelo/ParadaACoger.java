package es.usal.pa.modelo;

import java.io.Serializable;

public class ParadaACoger implements Serializable {
	
	 private int parada;
	 private float hora;
	 private String linea;
	 
	 public ParadaACoger() {
		 
	 }

	public int getParada() {
		return parada;
	}

	public void setParada(int parada) {
		this.parada = parada;
	}

	public float getHora() {
		return hora;
	}

	public void setHora(float hora) {
		this.hora = hora;
	}

	public String getLinea() {
		return linea;
	}

	public void setLinea(String linea) {
		this.linea = linea;
	}

	
	 
	 

}
