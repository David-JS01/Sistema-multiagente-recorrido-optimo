package es.usal.pa.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recorrido implements Serializable{
	
	private List <ParadaACoger> secuenciaParadas;
	private float horasTotales;
    
    public Recorrido () {
    	this.secuenciaParadas=new ArrayList<>();
    }

	public List<ParadaACoger> getSecuenciaParadas() {
		return secuenciaParadas;
	}

	public void setSecuenciaParadas(List<ParadaACoger> secuenciaParadas) {
		this.secuenciaParadas = secuenciaParadas;
	}

	public float getHorasTotales() {
		return horasTotales;
	}

	public void setHorasTotales(float horasTotales) {
		this.horasTotales = horasTotales;
	}

	
    
    

}
