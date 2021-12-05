package es.usal.pa.agent;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import es.usal.pa.modelo.Linea;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Clase general para las 2 lineas que contiene sus datos y que permite rellenar el objeto de la clase linea
 * para devolverselo al agente ruta
 * Autores David Jimenez Sanchez y Diego Gutierrez Martin.
 */
public class CyclicBehaviourLinea extends CyclicBehaviour
{
	
	
	/**
	 * Tiempo que se tarda entre parada y parada. Cada línea conoce los tiempos que tardan
	 * sus autobuses y no los del resto;
	 */
	protected Float tiempo[];
	
	/**
	 * hora de inicio de cada recorrido para la línea.
	 */
	protected Float inicioLinea[];
	
	/**
	 * Conexiones entre paradas de la línea 
	 */
	protected Integer parada[];
	
	protected Integer linea;
	
	
	
	
	
	
	public CyclicBehaviourLinea(Agent agent)
	{
		super(agent);
	}

	
	
	
	public Float[] getTiempo()
	{
		return tiempo;
	}
	public void setTiempo(Float[] tiempo)
	{
		this.tiempo = tiempo;
	}
	public Integer[] getParada()
	{
		return parada;
	}
	public void setParada(Integer[] parada)
	{
		this.parada = parada;
	}


	@Override
	public void action()
	{
		// TODO Auto-generated method stub
        ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
        try
		{
        	
        	
        	Linea linea = new Linea (Arrays.asList(inicioLinea),Arrays.asList(parada),Arrays.asList(tiempo),"linea"+this.linea,this.linea);
        	
           	ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
       		aclMessage.addReceiver(msg.getSender());
            aclMessage.setOntology("ontologia"+this.linea);
            //el lenguaje que se define para el servicio
            aclMessage.setLanguage(new SLCodec().getName());
            //el mensaje se transmita en XML
            aclMessage.setEnvelope(new Envelope());
			//cambio la codificacion de la carta
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
            //aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML); 
    		aclMessage.setContentObject((Serializable)linea);
    		this.myAgent.send(aclMessage);  
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
		

}
