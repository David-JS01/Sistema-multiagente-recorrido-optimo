package es.usal.pa.agent;

import java.util.Scanner;

import es.usal.pa.modelo.Recorrido;
import es.usal.pa.modelo.Usuario;
import es.usal.pa.modelo.ParadaACoger;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * El agente usuario solicita el recorridoMinimo a partir de las paradas origen y destino
 * y la hora.
 * Autores David Jimenez Sanchez y Diego Gutierrez Martin.
 */
public class AgentUsuario extends Agent
{

	protected void setup()
	{
	
		Usuario usuario=new Usuario();
		Scanner scanner=new Scanner(System.in);
		
		// programar con 1, 10, 2 por ejemplo.
		System.out.print("\nOrigen: ");
		usuario.setParadaOrigen(new Integer(scanner.nextLine()));
		System.out.print("\nDestino: ");
		usuario.setParadaDestino(new Integer(scanner.nextLine()));
		System.out.print("\nHora: ");
		usuario.setHora(new Float(scanner.nextLine()));
		
		Utils.enviarMensaje(this, "ruta", usuario);
		
		ACLMessage msg=blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		Recorrido recorrido=null;
		try
		{
			recorrido = (Recorrido)msg.getContentObject();
			imprimirRecorrido(recorrido);
		}
		catch (UnreadableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void imprimirRecorrido(Recorrido recorridoOptimo)
	{
		System.out.println("Recorrido: ");
		for (ParadaACoger p:recorridoOptimo.getSecuenciaParadas()){
			if (p.getLinea().equalsIgnoreCase("fin de trayecto"))
				System.out.println("Parada: "+p.getParada()+" Llegada: "+p.getHora());
			else
				System.out.println("Parada: "+p.getParada()+" "+p.getLinea()+" hora: "+p.getHora());
        }
	}

}
