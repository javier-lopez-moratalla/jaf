package jaf.agent;

import java.util.Date;
import java.util.List;

public class LifeCycle {

	private Date firstExecution;
	private long period;
	private List<String> acceptedSemantics;
	
	public Date getFirstExecution() {
		return firstExecution;
	}

	public void setFirstExecution(Date firstExecution) {
		this.firstExecution = firstExecution;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public void setAcceptedSemantics(List<String> acceptedSemantics) {
		this.acceptedSemantics = acceptedSemantics;
	}

	/**
	 * Período de ejecución del agente en milisegundos. -1 para terminar la ejecución del
	 * agente.
	 * @return
	 */
	public long getPeriod(){
		
		return period;
	}

	public List<String> getAcceptedSemantics() {
		return acceptedSemantics;
	}
}
