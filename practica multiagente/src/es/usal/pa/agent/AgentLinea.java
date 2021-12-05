package es.usal.pa.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/*
 * El agente linea proporciona los datos al agente ruta
 * Autores David Jimenez Sanchez y Diego Gutierrez Martin.
 */
public class AgentLinea extends Agent
{
	@Override
	protected void setup()
	{
		//Crear servicios proporcionados por el agentes y registrarlos en la plataforma
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName(getAID());
	    ServiceDescription sd = new ServiceDescription();
	    sd.setName("Linea");
	    //establezco el tipo del servicio para poder localizarlo cuando haga una busqueda
	    sd.setType("linea");
	    // Agents that want to use this service need to "know" the ontologia
	    sd.addOntologies("ontologia");
	    // Agents that want to use this service need to "speak" the FIPA-SL language
	    sd.addLanguages(new SLCodec().getName());
	    dfd.addServices(sd);
	    
	    try
	    {
	    	//registro los servicios
	        DFService.register(this, dfd);
	    }
	    catch(FIPAException e)
	    {
	        System.err.println("Agente "+getLocalName()+": "+e.getMessage());
	    }
	    
	    //si el nombre del agente es linea1 entonces se establece el cyclicbehaviourlinea1
	    if(getLocalName().equals("linea1"))
	    	addBehaviour(new CyclicBehaviourLinea1(this));
	    else
	    	addBehaviour(new CyclicBehaviourLinea2(this));
	}
}
