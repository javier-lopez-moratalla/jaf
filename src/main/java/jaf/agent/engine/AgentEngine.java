package jaf.agent.engine;

import jaf.agent.Agent;
import jaf.agent.task.AgentExecutionTask;
import jams.message.ReceiverID;
import jams.message.bus.Bus;
import jasa.schedule.Schedule;

import java.util.HashMap;
import java.util.Map;

public class AgentEngine {

	private Map<ReceiverID,AgentExecutionTask> agents;
	
	private Bus messageBus;
	private Schedule schedule;
	
	public Bus getMessageBus() {
		return messageBus;
	}
	public void setMessageBus(Bus messageBus) {
		this.messageBus = messageBus;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	
	public AgentEngine() {
	
		this.agents = new HashMap<ReceiverID, AgentExecutionTask>();
	}
	public void init(){
		
		Thread thread = new Thread(new ScheduleRunnable());
		thread.setDaemon(true);		
		thread.start();		
	}
	
	public void addAgent(Agent agent){
		
		messageBus.addReceiver(agent.getId(), agent);		
		AgentExecutionTask task = new AgentExecutionTask(agent);
		agents.put(agent.getId(), task);
		
		schedule.addTask(task, agent.getLifeCycle().getFirstExecution());
	}
	
	public void removeAgent(Agent agent){
		
		messageBus.removeReceiver(agent.getId());
		AgentExecutionTask task = agents.get(agent.getId());
		
		if(task != null){
		
			agents.remove(task);
			schedule.removeTask(task, task.getNextExecution());
		}
	}
	
	private class ScheduleRunnable implements Runnable{
		
		@Override
		public void run() {
		
			schedule.start();
		}
	}
}
