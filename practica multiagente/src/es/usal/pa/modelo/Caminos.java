package es.usal.pa.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Caminos implements Serializable{
	
	protected List <Integer> paradas;
	protected List <Float> tiempos;
	protected int lineaUsadaEnParada4;
	protected int lineaUsadaEnParada9;
    
    public Caminos () {
        paradas=new ArrayList<>();
        tiempos=new ArrayList<>();
    }

	public List<Integer> getParadas() {
		return paradas;
	}

	public void setParadas(List<Integer> paradas) {
		this.paradas = paradas;
	}

	public List<Float> getTiempos() {
		return tiempos;
	}

	public void setTiempos(List<Float> tiempos) {
		this.tiempos = tiempos;
	}

	public int getLineaUsadaEnParada4() {
		return lineaUsadaEnParada4;
	}

	public void setLineaUsadaEnParada4(int lineaUsadaEnParada4) {
		this.lineaUsadaEnParada4 = lineaUsadaEnParada4;
	}

	public int getLineaUsadaEnParada9() {
		return lineaUsadaEnParada9;
	}

	public void setLineaUsadaEnParada9(int lineaUsadaEnParada9) {
		this.lineaUsadaEnParada9 = lineaUsadaEnParada9;
	}
    
	
    
    

}
