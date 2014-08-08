package jaf.agent.task;

import java.util.Date;

import jaf.agent.Agent;
import jaf.agent.LifeCycle;
import jasa.schedule.Schedule;
import jasa.schedule.Task;

public class AgentExecutionTask implements Task {

	private Agent agent;
	
	@Override
	public void execute(Schedule schedule) {

		agent.execute();
		
		LifeCycle lifeCycle = agent.getLifeCycle();
		long period = lifeCycle.getPeriod();
		if(period != -1){
			
			Date now = new Date();
			now.setTime(now.getTime()+period);
			schedule.addTask(this, now);
		}		
	}
}
