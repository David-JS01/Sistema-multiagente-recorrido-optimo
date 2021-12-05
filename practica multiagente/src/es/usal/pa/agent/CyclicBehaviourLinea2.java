package es.usal.pa.agent;

import java.io.IOException;
import java.io.Serializable;
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
 * Autores David Jimenez Sanchez y Diego Gutierrez Martin.
 *
 */
public class CyclicBehaviourLinea2 extends CyclicBehaviourLinea
{
	public CyclicBehaviourLinea2(Agent agent)
	{
		super(agent);
		
		inicioLinea=new Float[]{1.0f, 8.0f, 15.0f, 320.0f, 350.0f};
		parada=  new Integer[]{11, 4, 12, 9, 13}; 		
		tiempo=new Float[]{3.0f, 4.0f, 3.0f, 2.0f};
		linea=2;
		
	}
}
