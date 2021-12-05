package es.usal.pa.modelo;

import java.io.Serializable;

public class Usuario implements Serializable{
	private int paradaOrigen;
	private int paradaDestino;
	private float hora;
	
	public Usuario () {
		
	}
	
	
	public Usuario(int paradaOrigen, int paradaDestino, float hora) {
		this.paradaOrigen = paradaOrigen;
		this.paradaDestino = paradaDestino;
		this.hora = hora;
	}


	public int getParadaOrigen() {
		return paradaOrigen;
	}


	public void setParadaOrigen(int paradaOrigen) {
		this.paradaOrigen = paradaOrigen;
	}


	public int getParadaDestino() {
		return paradaDestino;
	}


	public void setParadaDestino(int paradaDestino) {
		this.paradaDestino = paradaDestino;
	}


	public float getHora() {
		return hora;
	}


	public void setHora(float hora) {
		this.hora = hora;
	}
	
	

	
	
}
