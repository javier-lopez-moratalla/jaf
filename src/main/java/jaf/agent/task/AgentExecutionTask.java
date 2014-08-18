package jaf.agent.task;

import java.util.Date;

import jaf.agent.Agent;
import jaf.agent.LifeCycle;
import jasa.schedule.Schedule;
import jasa.schedule.Task;

public class AgentExecutionTask implements Task {

	private Agent agent;
	private Date nextExecution;
	
	public AgentExecutionTask(Agent agent) {
		super();
		this.agent = agent;
		this.nextExecution = agent.getLifeCycle().getFirstExecution();
	}

	public Date getNextExecution() {
		return nextExecution;
	}
	
	@Override
	public void execute(Schedule schedule) {

		agent.execute();
		
		LifeCycle lifeCycle = agent.getLifeCycle();
		long period = lifeCycle.getPeriod();
		if(period != -1){
			
			nextExecution = new Date();
			nextExecution.setTime(nextExecution.getTime()+period);
			schedule.addTask(this, nextExecution);
		}		
	}
}
