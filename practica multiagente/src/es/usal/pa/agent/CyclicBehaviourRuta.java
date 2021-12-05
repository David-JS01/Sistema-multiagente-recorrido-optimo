package es.usal.pa.agent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import es.usal.pa.modelo.*;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Calculara a partir de los datos obtenidos de la lineas y de los enviados por el usuario un recorrido
 * minimo y lo devolvera
 * Autores David Jimenez Sanchez y Diego Gutierrez Martin.
 */
public class CyclicBehaviourRuta extends CyclicBehaviour
{
	
	private Linea linea1;
    private Linea linea2;
    private List <Float> horaBusesParada4Linea1;
    private List <Float> horaBusesParada4Linea2;
    private List <Float> horaBusesParada9Linea1;
    private List <Float> horaBusesParada9Linea2;
    private List <Float> horaBusesParadaOrigen;
    private List <Caminos> alternativa;  
    private Recorrido recorridoMinimo;
    private List <Recorrido> posiblesRecorridosMinimos;
	
	public CyclicBehaviourRuta(Agent agent) //inicializamos
	{
		super(agent);
		horaBusesParada4Linea1=new ArrayList<>();
        horaBusesParada4Linea2=new ArrayList<>();
        horaBusesParada9Linea1=new ArrayList<>();
        horaBusesParada9Linea2=new ArrayList<>();
        horaBusesParadaOrigen=new ArrayList<>();
        recorridoMinimo=new Recorrido ();
        alternativa= new ArrayList<>();
        posiblesRecorridosMinimos= new ArrayList<>();
        linea1=new Linea();
        linea2=new Linea();
		
	}
	
	@Override
	public void action()
	{
		int origen;
		int destino;
		float hora;
        ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        try
		{
    		//mandar mensajes a todas las líneas
        	Utils.enviarMensaje(this.myAgent, "linea", null);
 
        	//esperamos por mensaje inform con ontologia1 y otro con ontologia2 para evitar que se repitan los datos de una linea
        	ACLMessage msgLineaA=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia1")));
        	ACLMessage msgLineaB=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia2")));
        
        	//obtenemos los datos y los guardamos en objetos de la clase Linea
        	linea1=(Linea)msgLineaA.getContentObject();  
    		
    		linea2=(Linea)msgLineaB.getContentObject();
        	
    		//calcular el recorrido más corto
    		Usuario usuario=(Usuario)msg.getContentObject();
    		origen=usuario.getParadaOrigen();
    		destino=usuario.getParadaDestino();
    		hora=usuario.getHora();
    		int lin;
    		calcularHorariosBusesParadasConjAcortado();
    		if (origen==4 || origen == 9)
                lin=0;
            else if (origen==11 || origen==12 || origen==13)
                lin=2;
            else
                lin=1;
    		
    		calcularHorariosBusesParadaOrigen(lin, origen);
    		
    		if (origen == 4){ // eliminar las horas de la linea 2
                if (destino >= 5 && destino <= 8){
                    List<Float> aux = new ArrayList<>();
                    int numeroBusesLinea1=linea1.getInicioLinea().size();
                    for (int i=0; i<numeroBusesLinea1; i++){
                        aux.add(horaBusesParadaOrigen.get(i));
                    }
                    horaBusesParadaOrigen=aux;
                }
            }
    		
    		eliminarHorasMenoresQueLaDeSalida(hora);
    		
    		generarCaminosAlternativos();
    		
    		eliminarInnecesarios(origen, destino);
    		Recorrido rec = new Recorrido();
    		for (Caminos c : alternativa){
                rec=calculoRecorridoParaAlternativa(c, origen, destino);
                posiblesRecorridosMinimos.add(rec);
            }

    		recorridoMinimo=posiblesRecorridosMinimos.get(0);
            for (Recorrido re: posiblesRecorridosMinimos){
                if (rec.getHorasTotales()<recorridoMinimo.getHorasTotales())
                    recorridoMinimo = re;
            }
            
            rellenarCaminoMinimo();

            //devolver el resultado al usuario
           	ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
       		aclMessage.addReceiver(msg.getSender());
            aclMessage.setOntology("ontologia");
            //el lenguaje que se define para el servicio
            aclMessage.setLanguage(new SLCodec().getName());
            //el mensaje se transmita en XML
            aclMessage.setEnvelope(new Envelope());
			//cambio la codificacion de la carta
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
            //aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML); 
    		aclMessage.setContentObject((Serializable)recorridoMinimo);
    		this.myAgent.send(aclMessage);  
   		
    		recorridoMinimo=null;
    		
			
		}
		catch (UnreadableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	private void calcularHorariosBusesParadasConjAcortado(){
        horaBusesParada4Linea1.clear();
        horaBusesParada4Linea2.clear();
        horaBusesParada9Linea1.clear();
        horaBusesParada9Linea2.clear();
        for (float hora : linea1.getInicioLinea()){
            horaBusesParada4Linea1.add(hora+7);
        }
        for (float hora : linea2.getInicioLinea()){
            horaBusesParada4Linea2.add(hora+3);
        }
        for (float hora : linea1.getInicioLinea()){
            horaBusesParada9Linea1.add(hora+24);
        }
        for (float hora : linea2.getInicioLinea()){
            horaBusesParada9Linea2.add(hora+10);
        }
    } //calcula las horas alas que llegan los buses de manera mas eficiente
	
	 private void calcularHorariosBusesParadaOrigen (int linea, int origen){
	        int paradaActual;
	        int a;
	        switch(linea){
	            case 0:
	                 for (float h : linea1.getInicioLinea()){ //para cada bus
	                     paradaActual = linea1.getParada().get(0);
	                     a=0;
	                    while (paradaActual != origen){
	                        h+=linea1.getTiempos().get(a);
	                        a++;
	                        paradaActual=linea1.getParada().get(a);
	                    }
	                horaBusesParadaOrigen.add(h); //añadimos la hora a la que llega a dicha parada el bus
	                }
	            
	                for (float h : linea2.getInicioLinea()){ //para cada bus
	                    paradaActual = linea2.getParada().get(0);
	                    a=0;
	                    while (paradaActual != origen){
	                        h+=linea2.getTiempos().get(a);
	                        a++;
	                        paradaActual=linea2.getParada().get(a);
	                    }
	                    horaBusesParadaOrigen.add(h); //añadimos la hora a la que llega a dicha parada el bus
	                }
	                break;
	            case 1:
	                for (float h : linea1.getInicioLinea()){ //para cada bus
	                     paradaActual = linea1.getParada().get(0);
	                     a=0;
	                    while (paradaActual != origen){
	                        h+=linea1.getTiempos().get(a);
	                        a++;
	                        paradaActual=linea1.getParada().get(a);
	                    }
	                horaBusesParadaOrigen.add(h); //añadimos la hora a la que llega a dicha parada el bus
	                }
	                break;
	            case 2:
	                for (float h : linea2.getInicioLinea()){ //para cada bus
	                    paradaActual = linea2.getParada().get(0);
	                    a=0;
	                    while (paradaActual != origen){
	                        h+=linea2.getTiempos().get(a);
	                        a++;
	                        paradaActual=linea2.getParada().get(a);
	                    }
	                    horaBusesParadaOrigen.add(h); //añadimos la hora a la que llega a dicha parada el bus
	                }
	                break;
	             
	        }
	    }//calcula horas de llegada de buses a origen
	 
	 private void eliminarHorasMenoresQueLaDeSalida (float hora){
	        Iterator <Float> it = horaBusesParadaOrigen.iterator();
	        while (it.hasNext()){
	            if (it.next()<hora)
	                it.remove();
	        }
	        
	    } //elimina aquellos buses que ya hallan pasado
	 
	 private void generarCaminosAlternativos() {
	        Caminos c;
	        int linea4, linea9;
	        int paradas1 []={1,2,3,4,5,6,7,8,9,10}; //linea 1 linea 1
	        int paradas2 []={1,2,3,4,5,6,7,8,9,13}; //linea 1 linea 2
	        int paradas3 []={1,2,3,4,12,9,10}; //linea 2 linea 1
	        int paradas4 []={1,2,3,4,12,9,13}; //linea 2 linea 2
	        int paradas5 []={11,4,5,6,7,8,9,10}; //linea 1 linea 1
	        int paradas6 []={11,4,5,6,7,8,9,13}; //linea 1 linea 2
	        int paradas7 []={11,4,12,9,10}; //linea 2 linea 1
	        int paradas8 []={11,4,12,9,13}; //linea 2 linea 2
	        int [][] paradas={paradas1, paradas2, paradas3, paradas4, paradas5, paradas6, paradas7, paradas8};
	        
	        float tiempos1 []={2,3,2,7,1,4,3,2,1};
	        float tiempos2 []={2,3,2,7,1,4,3,2,2};
	        float tiempos3 []={2,3,2,4,3,1};
	        float tiempos4 []={2,3,2,4,3,2};
	        float tiempos5 []={3,7,1,4,3,2,1};
	        float tiempos6 []={3,7,1,4,3,2,2};
	        float tiempos7 []={3,4,3,1};
	        float tiempos8 []={3,4,3,2};
	        float [][] tiempos = {tiempos1, tiempos2, tiempos3, tiempos4, tiempos5, tiempos6, tiempos7, tiempos8};
	        
	        
	        for (int i=0; i<8; i++){
	            c=new Caminos();
	            rellenar (c.getParadas(),paradas[i]);
	            rellenar (c.getTiempos(),tiempos[i]);
	            alternativa.add(c);
	        }
	       alternativa.get(0).setLineaUsadaEnParada4(1);
	       alternativa.get(0).setLineaUsadaEnParada9(1);
	       
	       alternativa.get(1).setLineaUsadaEnParada4(1);
	       alternativa.get(1).setLineaUsadaEnParada9(2);
	       
	       alternativa.get(2).setLineaUsadaEnParada4(2);
	       alternativa.get(2).setLineaUsadaEnParada9(1);
	       
	       alternativa.get(3).setLineaUsadaEnParada4(2);
	       alternativa.get(3).setLineaUsadaEnParada9(2);
	       
	       alternativa.get(4).setLineaUsadaEnParada4(1);
	       alternativa.get(4).setLineaUsadaEnParada9(1);
	       
	       alternativa.get(5).setLineaUsadaEnParada4(1);
	       alternativa.get(5).setLineaUsadaEnParada9(2);
	       
	        alternativa.get(6).setLineaUsadaEnParada4(2);
	       alternativa.get(6).setLineaUsadaEnParada9(1);
	       
	       alternativa.get(7).setLineaUsadaEnParada4(2);
	       alternativa.get(7).setLineaUsadaEnParada9(2);
	        
	    }
	 
	 private void rellenar(List<Integer> paradas, int[] paradas1) {
	        for (int i=0; i<paradas1.length; i++){
	            paradas.add(paradas1[i]);
	        }
	    }
	 private void rellenar(List<Float> tiempos, float[] tiempos1) {
	        for (int i=0; i<tiempos1.length; i++){
	            tiempos.add(tiempos1[i]);
	        }
	    }
	 
	 private void eliminarInnecesarios(int origen, int destino) {
	        Iterator <Caminos> i = alternativa.iterator();
	        Caminos c;
	        boolean origenEncontrado=false;
	        boolean destinoEncontrado=false;
	        while (i.hasNext()){
	            c=(Caminos)i.next();
	            for (int p:c.getParadas()){
	                if (p==origen ){
	                    origenEncontrado=true;
	                }else if (p==destino){
	                    destinoEncontrado=true;
	                }
	            }
	            if (!origenEncontrado || !destinoEncontrado)
	                i.remove();
	            origenEncontrado=false;
	            destinoEncontrado=false;
	        }
	    }
	 
	 private Recorrido calculoRecorridoParaAlternativa(Caminos c, int origen, int destino) {
	        Recorrido recorrido = new Recorrido();
	        Linea lin;
	        int o=c.getParadas().indexOf(origen)+1;
	        int d=c.getParadas().indexOf(destino);
	        ParadaACoger pac = new ParadaACoger();
	        String linea;
	        if (origen==11 || origen==12 || origen==13){
	            linea="linea 2";
	            lin=linea1;
	        }else{
	            linea="linea 1";
	            lin=linea2;
	        }
	        pac.setLinea(linea);
	        pac.setParada(origen);
	        pac.setHora(Collections.min(horaBusesParadaOrigen));
	        recorrido.getSecuenciaParadas().add(pac);
	        recorrido.setHorasTotales(pac.getHora());
	        for (int i=o; i<=d; i++){
	            pac=new ParadaACoger();
	            pac.setParada(c.getParadas().get(i)); //añade la parada siguiente del camino
	            recorrido.setHorasTotales(recorrido.getHorasTotales()+c.getTiempos().get(i-1));
	            if (pac.getParada()==4){
	                linea = "linea "+c.getLineaUsadaEnParada4();
	                pac.setLinea(linea);
	                if (destino==4){
	                   pac.setHora(recorrido.getHorasTotales()); 
	                }else{
	                    if (c.getLineaUsadaEnParada4()==1){
	                        List <Float> aux = new ArrayList<>();
	                        aux.addAll(horaBusesParada4Linea1);
	                        horaBusesParada4Linea1=limpiarHoraBusesParadaConjunta(recorrido.getHorasTotales(), horaBusesParada4Linea1);
	                        if (horaBusesParada4Linea1.size()>0)
	                            pac.setHora(Collections.min(horaBusesParada4Linea1));
	                        else{
	                            recorrido.setHorasTotales(Float.MAX_VALUE);
	                            return recorrido;
	                        }
	                        horaBusesParada4Linea1=aux;
	                        recorrido.setHorasTotales(pac.getHora());
	                    }else{
	                        List <Float> aux = new ArrayList<>();
	                        aux.addAll(horaBusesParada4Linea2);
	                        horaBusesParada4Linea2=limpiarHoraBusesParadaConjunta(recorrido.getHorasTotales(), horaBusesParada4Linea2);
	                        if (horaBusesParada4Linea2.size()>0)
	                            pac.setHora(Collections.min(horaBusesParada4Linea2));
	                        else{
	                            recorrido.setHorasTotales(Float.MAX_VALUE);
	                            return recorrido;
	                        }
	                        horaBusesParada4Linea2=aux;
	                        recorrido.setHorasTotales(pac.getHora());
	                    }
	                }
	            }else if (pac.getParada()==9){
	                linea = "linea "+c.getLineaUsadaEnParada9();
	                pac.setLinea(linea);
	                if (destino==9){
	                    pac.setHora(recorrido.getHorasTotales());
	                }else{
	                    if (c.getLineaUsadaEnParada9()==1){
	                        List <Float> aux = new ArrayList<>();
	                        aux.addAll(horaBusesParada9Linea1);
	                        horaBusesParada9Linea1=limpiarHoraBusesParadaConjunta(recorrido.getHorasTotales(), horaBusesParada9Linea1);
	                        if (horaBusesParada9Linea1.size()>0)
	                            pac.setHora(Collections.min(horaBusesParada9Linea1));
	                        else{
	                            recorrido.setHorasTotales(Float.MAX_VALUE);
	                            return recorrido;
	                        }
	                        horaBusesParada9Linea1=aux;
	                        recorrido.setHorasTotales(pac.getHora());
	                    }else{
	                        List <Float> aux = new ArrayList<>();
	                        aux.addAll(horaBusesParada9Linea2);
	                        horaBusesParada9Linea2=limpiarHoraBusesParadaConjunta(recorrido.getHorasTotales(), horaBusesParada9Linea2);
	                        if (horaBusesParada9Linea2.size()>0)
	                            pac.setHora(Collections.min(horaBusesParada9Linea2));
	                        else{
	                            recorrido.setHorasTotales(Float.MAX_VALUE);
	                            return recorrido;
	                        }
	                        horaBusesParada9Linea2=aux;
	                        recorrido.setHorasTotales(pac.getHora());
	                    }
	                }
	            }else if (pac.getParada()==11 || pac.getParada()==12 || pac.getParada()==13){
	                linea = "linea 2";
	                pac.setLinea(linea);
	                pac.setHora(recorrido.getHorasTotales()); //añade la hora a la que lo toma
	            }else{
	                linea = "linea 1";
	                pac.setLinea(linea);
	                pac.setHora(recorrido.getHorasTotales()); //añade la hora a la que lo toma
	            }
	            recorrido.getSecuenciaParadas().add(pac);
	            
	        }
	        
	        return recorrido;
	    }
	 
	 private List<Float> limpiarHoraBusesParadaConjunta(float hora, List<Float> horaBusesParada) {
	        List<Float> aux = new ArrayList<>();
	        aux = horaBusesParada;
	        Iterator<Float> i = aux.iterator();
	        while (i.hasNext()){
	            if ((float)i.next()<hora)
	                i.remove();
	        }
	        return aux;
	    }
	 
	 private void rellenarCaminoMinimo() {
	        int j=recorridoMinimo.getSecuenciaParadas().size()-1;
	        int sigParada;
	        for (int i=0; i<j; i++){
	            sigParada=recorridoMinimo.getSecuenciaParadas().get(i+1).getParada();
	            if (sigParada>=12 && sigParada<=13){
	                recorridoMinimo.getSecuenciaParadas().get(i).setLinea("coger linea: 2");
	            }else if((sigParada==9 || sigParada==4) && i!=0 ) {
	            	recorridoMinimo.getSecuenciaParadas().get(i).setLinea(recorridoMinimo.getSecuenciaParadas().get(i-1).getLinea());
	            }else{
	                recorridoMinimo.getSecuenciaParadas().get(i).setLinea("coger linea: 1");
	            }
	        }
	        recorridoMinimo.getSecuenciaParadas().get(j).setLinea("fin de trayecto");
	    }

	
}