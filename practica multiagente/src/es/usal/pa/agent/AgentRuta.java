package es.usal.pa.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * El agente ruta recibe el mensaje del agente usuario, tendra 5 comportamientos ciclicos
 * paralelos en los que se calculara el recorrido minimo
 * para ellos preguntara a los agentes linea por sus datos
 * Autores David Jimenez Sanchez y Diego Gutierrez Martin.
 */
public class AgentRuta extends Agent
{
	protected ParallelBehaviour parallelBehaviour; 
	
	@Override
	protected void setup()
	{
		//Creamos servicios proporcionados por el agentes y los registramos en la plataforma
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName(getAID());
	    ServiceDescription sd = new ServiceDescription();
	    sd.setName("Ruta");
	    //establecemos el tipo del servicio para poder localizarlo cuando haga una busqueda el agente usuario
	    sd.setType("ruta");
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
	    
	    
	    //Creamos una parallel behaviour para introducir en ella 5 cyclic behaviour
	    parallelBehaviour=new ParallelBehaviour();
	    ThreadedBehaviourFactory tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));
	    
	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));
	    
	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));

	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));

	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));
	    
	    addBehaviour(parallelBehaviour);
	}
}
