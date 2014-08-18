package jaf.agent;

import java.util.Date;
import java.util.List;

public interface LifeCycle {

	public Date getFirstExecution();
	
	/**
	 * Per�odo de ejecuci�n del agente en milisegundos
	 * @return
	 */
	public long getPeriod();
	
	public List<String> getAcceptedSemantics();
}
